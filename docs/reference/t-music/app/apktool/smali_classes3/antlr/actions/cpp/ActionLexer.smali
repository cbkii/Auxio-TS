.class public Lantlr/actions/cpp/ActionLexer;
.super Lantlr/CharScanner;
.source ""

# interfaces
.implements Lantlr/actions/cpp/ActionLexerTokenTypes;
.implements Lantlr/TokenStream;


# static fields
.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_1:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_10:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_11:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_12:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_13:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_14:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_15:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_16:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_17:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_18:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_19:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_2:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_20:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_21:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_22:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_23:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_24:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_25:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_26:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_3:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_4:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_5:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_6:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_7:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_8:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_9:Lantlr/collections/impl/BitSet;


# instance fields
.field public antlrTool:Lantlr/Tool;

.field public currentRule:Lantlr/RuleBlock;

.field public generator:Lantlr/CodeGenerator;

.field public lineOffset:I

.field public transInfo:Lantlr/ActionTransInfo;


# direct methods
.method public static constructor <clinit>()V
    .locals 2

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_1()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_2()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_3()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_4()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_5()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_6()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_7()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_7:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_8()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_8:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_9()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_9:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_10()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_10:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_11()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_11:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_12()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_12:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_13()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_13:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_14()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_14:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_15()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_15:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_16()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_16:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_17()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_17:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_18()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_18:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_19()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_19:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_20()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_20:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_21()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_21:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_22()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_22:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_23()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_23:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_24()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_24:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_25()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_25:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/cpp/ActionLexer;->mk_tokenSet_26()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/cpp/ActionLexer;->_tokenSet_26:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 1

    new-instance v0, Lantlr/LexerSharedInputState;

    invoke-direct {v0, p1}, Lantlr/LexerSharedInputState;-><init>(Lantlr/InputBuffer;)V

    invoke-direct {p0, v0}, Lantlr/actions/cpp/ActionLexer;-><init>(Lantlr/LexerSharedInputState;)V

    return-void
.end method

.method public constructor <init>(Lantlr/LexerSharedInputState;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/CharScanner;-><init>(Lantlr/LexerSharedInputState;)V

    const/4 p1, 0x0

    iput p1, p0, Lantlr/actions/cpp/ActionLexer;->lineOffset:I

    const/4 p1, 0x1

    iput-boolean p1, p0, Lantlr/CharScanner;->caseSensitiveLiterals:Z

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->setCaseSensitive(Z)V

    new-instance p1, Ljava/util/Hashtable;

    invoke-direct {p1}, Ljava/util/Hashtable;-><init>()V

    iput-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    return-void
.end method

.method public constructor <init>(Ljava/io/InputStream;)V
    .locals 1

    new-instance v0, Lantlr/ByteBuffer;

    invoke-direct {v0, p1}, Lantlr/ByteBuffer;-><init>(Ljava/io/InputStream;)V

    invoke-direct {p0, v0}, Lantlr/actions/cpp/ActionLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public constructor <init>(Ljava/io/Reader;)V
    .locals 1

    new-instance v0, Lantlr/CharBuffer;

    invoke-direct {v0, p1}, Lantlr/CharBuffer;-><init>(Ljava/io/Reader;)V

    invoke-direct {p0, v0}, Lantlr/actions/cpp/ActionLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;Lantlr/RuleBlock;Lantlr/CodeGenerator;Lantlr/ActionTransInfo;)V
    .locals 1

    new-instance v0, Ljava/io/StringReader;

    invoke-direct {v0, p1}, Ljava/io/StringReader;-><init>(Ljava/lang/String;)V

    invoke-direct {p0, v0}, Lantlr/actions/cpp/ActionLexer;-><init>(Ljava/io/Reader;)V

    iput-object p2, p0, Lantlr/actions/cpp/ActionLexer;->currentRule:Lantlr/RuleBlock;

    iput-object p3, p0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    iput-object p4, p0, Lantlr/actions/cpp/ActionLexer;->transInfo:Lantlr/ActionTransInfo;

    return-void
.end method

.method public static final mk_tokenSet_0()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x1800000008L

    aput-wide v2, v0, v1

    const/4 v1, 0x1

    :goto_0
    const/4 v2, 0x3

    if-gt v1, v2, :cond_0

    const-wide/16 v2, -0x1

    aput-wide v2, v0, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    return-object v0
.end method

.method public static final mk_tokenSet_1()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x840000000008L

    aput-wide v2, v0, v1

    const/4 v1, 0x1

    :goto_0
    const/4 v2, 0x3

    if-gt v1, v2, :cond_0

    const-wide/16 v2, -0x1

    aput-wide v2, v0, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    return-object v0
.end method

.method public static final mk_tokenSet_10()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ff089400000000L
        0x7fffffe87fffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_11()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ff1a9500002600L
        0x7fffffea7fffffeL    # 3.7857645700037357E-270
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_12()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x400000000000000L
        0x7fffffe87fffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_13()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x3400ff0100002600L    # 3.3845454728352894E-58
        0x28000000
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_14()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x4000000000000008L    # -1.9999999999999982

    aput-wide v2, v0, v1

    const/4 v1, 0x1

    :goto_0
    const/4 v2, 0x3

    if-gt v1, v2, :cond_0

    const-wide/16 v2, -0x1

    aput-wide v2, v0, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    return-object v0
.end method

.method public static final mk_tokenSet_15()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ff038d00002600L
        0x7fffffe8ffffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_16()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x2000be0100002600L
        0x20000000
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_17()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x2000000100002600L    # 1.491669568808863E-154
        0x0
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_18()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0xbe0100002600L
        0x20000000
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_19()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x400010c00000000L
        0x7fffffe8ffffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_2()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x809c00002408L

    aput-wide v2, v0, v1

    const/4 v1, 0x1

    :goto_0
    const/4 v2, 0x3

    if-gt v1, v2, :cond_0

    const-wide/16 v2, -0x1

    aput-wide v2, v0, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    return-object v0
.end method

.method public static final mk_tokenSet_20()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0xac0100002600L
        0x0
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_21()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ffad8d00002600L
        0x7fffffe8ffffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_22()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ff7b9500002600L
        0x7fffffeaffffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_23()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ff0a9500002600L
        0x7fffffe87fffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_24()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ff089500002600L
        0x7fffffe87fffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_25()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7fffe9500002600L
        0x7fffffea7fffffeL    # 3.7857645700037357E-270
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_26()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ffbe9500002600L
        0x7fffffea7fffffeL    # 3.7857645700037357E-270
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_3()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x400000100002600L
        0x7fffffe87fffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_4()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x100002600L    # 2.122000597E-314
        0x0
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_5()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x10100002600L
        0x0
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_6()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ff099500002600L
        0x7fffffe87fffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_7()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x400000008L

    aput-wide v2, v0, v1

    const/4 v1, 0x1

    const-wide/32 v2, -0x10000001

    aput-wide v2, v0, v1

    const/4 v1, 0x2

    :goto_0
    const/4 v2, 0x3

    if-gt v1, v2, :cond_0

    const-wide/16 v2, -0x1

    aput-wide v2, v0, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    return-object v0
.end method

.method public static final mk_tokenSet_8()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x8000000008L

    aput-wide v2, v0, v1

    const/4 v1, 0x1

    const-wide/32 v2, -0x10000001

    aput-wide v2, v0, v1

    const/4 v1, 0x2

    :goto_0
    const/4 v2, 0x3

    if-gt v1, v2, :cond_0

    const-wide/16 v2, -0x1

    aput-wide v2, v0, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    return-object v0
.end method

.method public static final mk_tokenSet_9()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x7ff000000000000L
        0x7fffffe87fffffeL
        0x0
        0x0
        0x0
    .end array-data
.end method


# virtual methods
.method public final mACTION(Z)V
    .locals 6

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    const/4 v3, 0x1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    const/16 v5, 0x23

    if-eq v4, v5, :cond_4

    const/16 v5, 0x24

    if-eq v4, v5, :cond_3

    sget-object v4, Lantlr/actions/cpp/ActionLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v4, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v4

    if-eqz v4, :cond_0

    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mSTUFF(Z)V

    goto :goto_2

    :cond_0
    if-lt v2, v3, :cond_2

    if-eqz p1, :cond_1

    const/4 p1, 0x4

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_1

    :cond_1
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_2
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_3
    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ITEM(Z)V

    goto :goto_2

    :cond_4
    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mAST_ITEM(Z)V

    :goto_2
    add-int/lit8 v2, v2, 0x1

    goto :goto_0
.end method

.method public final mARG(Z)V
    .locals 16

    move-object/from16 v0, p0

    iget-object v1, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    const/4 v2, 0x1

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x22

    const/16 v5, 0x27

    const/16 v6, 0xff

    const/4 v7, 0x2

    const/4 v8, 0x0

    const/4 v9, 0x3

    if-eq v3, v5, :cond_2

    packed-switch v3, :pswitch_data_0

    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_19:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v10

    invoke-virtual {v3, v10}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-virtual {v0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v9, :cond_0

    invoke-virtual {v0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_0

    invoke-virtual {v0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v9, :cond_0

    invoke-virtual {v0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_0

    invoke-virtual {v0, v8}, Lantlr/actions/cpp/ActionLexer;->mTREE_ELEMENT(Z)V

    goto :goto_0

    :pswitch_0
    invoke-virtual {v0, v8}, Lantlr/actions/cpp/ActionLexer;->mINT_OR_FLOAT(Z)V

    goto :goto_0

    :cond_0
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v4, :cond_1

    invoke-virtual {v0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v9, :cond_1

    invoke-virtual {v0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_1

    invoke-virtual {v0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v9, :cond_1

    invoke-virtual {v0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_1

    invoke-virtual {v0, v8}, Lantlr/actions/cpp/ActionLexer;->mSTRING(Z)V

    goto :goto_0

    :cond_1
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_2
    invoke-virtual {v0, v8}, Lantlr/actions/cpp/ActionLexer;->mCHAR(Z)V

    :goto_0
    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_20:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v10

    invoke-virtual {v3, v10}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_c

    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_21:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v10

    invoke-virtual {v3, v10}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_c

    invoke-virtual {v0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v9, :cond_c

    invoke-virtual {v0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_c

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v10, 0x20

    const/16 v11, 0xd

    const/16 v12, 0xa

    const/16 v13, 0x9

    const/16 v14, 0x2f

    const/16 v15, 0x2d

    const/16 v6, 0x2b

    const/16 v7, 0x2a

    if-eq v3, v13, :cond_4

    if-eq v3, v12, :cond_4

    if-eq v3, v11, :cond_4

    if-eq v3, v10, :cond_4

    if-eq v3, v15, :cond_5

    if-eq v3, v14, :cond_5

    if-eq v3, v7, :cond_5

    if-ne v3, v6, :cond_3

    goto :goto_1

    :cond_3
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_4
    invoke-virtual {v0, v8}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :cond_5
    :goto_1
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v7, :cond_9

    if-eq v3, v6, :cond_8

    if-eq v3, v15, :cond_7

    if-ne v3, v14, :cond_6

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_6
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_7
    invoke-virtual {v0, v15}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_8
    invoke-virtual {v0, v6}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_9
    invoke-virtual {v0, v7}, Lantlr/CharScanner;->match(C)V

    :goto_2
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v13, :cond_a

    if-eq v3, v12, :cond_a

    if-eq v3, v11, :cond_a

    if-eq v3, v10, :cond_a

    const/16 v6, 0x5f

    if-eq v3, v6, :cond_b

    if-eq v3, v4, :cond_b

    const/16 v6, 0x23

    if-eq v3, v6, :cond_b

    if-eq v3, v5, :cond_b

    const/16 v6, 0x28

    if-eq v3, v6, :cond_b

    packed-switch v3, :pswitch_data_1

    packed-switch v3, :pswitch_data_2

    packed-switch v3, :pswitch_data_3

    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_a
    invoke-virtual {v0, v8}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :cond_b
    :pswitch_1
    invoke-virtual {v0, v8}, Lantlr/actions/cpp/ActionLexer;->mARG(Z)V

    const/16 v6, 0xff

    const/4 v7, 0x2

    goto/16 :goto_0

    :cond_c
    if-eqz p1, :cond_d

    const/16 v2, 0x10

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v2

    new-instance v3, Ljava/lang/String;

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v4

    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v5

    sub-int/2addr v5, v1

    invoke-direct {v3, v4, v1, v5}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {v2, v3}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_3

    :cond_d
    const/4 v2, 0x0

    :goto_3
    iput-object v2, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x30
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x30
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x41
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0x61
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch
.end method

.method public final mAST_CONSTRUCTOR(Z)V
    .locals 16

    move-object/from16 v0, p0

    iget-object v1, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const/16 v3, 0x5b

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(C)V

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    const/4 v2, 0x1

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x23

    const/16 v5, 0x22

    const/16 v6, 0x5f

    const/16 v7, 0x28

    const/16 v8, 0x20

    const/16 v9, 0xd

    const/4 v10, 0x0

    const/16 v11, 0x9

    const/16 v12, 0xa

    if-eq v3, v11, :cond_0

    if-eq v3, v12, :cond_0

    if-eq v3, v9, :cond_0

    if-eq v3, v8, :cond_0

    if-eq v3, v7, :cond_1

    if-eq v3, v6, :cond_1

    if-eq v3, v5, :cond_1

    if-eq v3, v4, :cond_1

    packed-switch v3, :pswitch_data_0

    packed-switch v3, :pswitch_data_1

    packed-switch v3, :pswitch_data_2

    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_0
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v10}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v13, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v13, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_1
    :pswitch_0
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->mAST_CTOR_ELEMENT(Z)V

    iget-object v13, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v13, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v3, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v13

    const/16 v14, 0x2c

    const/16 v15, 0x5d

    if-eq v13, v11, :cond_3

    if-eq v13, v12, :cond_3

    if-eq v13, v9, :cond_3

    if-eq v13, v8, :cond_3

    if-eq v13, v14, :cond_4

    if-ne v13, v15, :cond_2

    goto :goto_0

    :cond_2
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_3
    iget-object v13, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v13}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v13

    invoke-virtual {v0, v10}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v10, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v13}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_4
    :goto_0
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v10

    const/4 v13, 0x0

    if-eq v10, v14, :cond_6

    if-ne v10, v15, :cond_5

    move-object v4, v13

    goto/16 :goto_1

    :cond_5
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_6
    iget-object v10, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v10

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->match(C)V

    iget-object v14, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v14, v10}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v10

    if-eq v10, v11, :cond_7

    if-eq v10, v12, :cond_7

    if-eq v10, v9, :cond_7

    if-eq v10, v8, :cond_7

    if-eq v10, v7, :cond_8

    if-eq v10, v6, :cond_8

    if-eq v10, v5, :cond_8

    if-eq v10, v4, :cond_8

    packed-switch v10, :pswitch_data_3

    packed-switch v10, :pswitch_data_4

    packed-switch v10, :pswitch_data_5

    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_7
    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v4

    const/4 v5, 0x0

    invoke-virtual {v0, v5}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5, v4}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_8
    :pswitch_1
    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v4

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->mAST_CTOR_ELEMENT(Z)V

    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5, v4}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v4, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    if-eq v5, v11, :cond_a

    if-eq v5, v12, :cond_a

    if-eq v5, v9, :cond_a

    if-eq v5, v8, :cond_a

    if-ne v5, v15, :cond_9

    goto :goto_1

    :cond_9
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_a
    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const/4 v5, 0x0

    invoke-virtual {v0, v5}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_1
    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->match(C)V

    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v2, v0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Lantlr/CodeGenerator;->processStringForASTConstructor(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    if-eqz v4, :cond_b

    const-string v3, ","

    invoke-static {v2, v3}, La/a/a/a/a;->b(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-static {v4, v2}, La/a/a/a/a;->a(Lantlr/Token;Ljava/lang/StringBuilder;)Ljava/lang/String;

    move-result-object v2

    :cond_b
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    iget-object v4, v0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v4, v13, v2}, Lantlr/CodeGenerator;->getASTCreateString(Lantlr/GrammarAtom;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    if-eqz p1, :cond_c

    invoke-virtual {v0, v12}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v13

    new-instance v2, Ljava/lang/String;

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v3

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v4

    sub-int/2addr v4, v1

    invoke-direct {v2, v3, v1, v4}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {v13, v2}, Lantlr/Token;->setText(Ljava/lang/String;)V

    :cond_c
    iput-object v13, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :pswitch_data_0
    .packed-switch 0x30
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x41
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x61
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0x30
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_4
    .packed-switch 0x41
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_5
    .packed-switch 0x61
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch
.end method

.method public final mAST_CTOR_ELEMENT(Z)V
    .locals 8

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x0

    const/16 v4, 0xff

    const/4 v5, 0x2

    const/4 v6, 0x3

    const/16 v7, 0x22

    if-ne v2, v7, :cond_0

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_0

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v4, :cond_0

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_0

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v4, :cond_0

    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mSTRING(Z)V

    goto :goto_0

    :cond_0
    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_19:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v7

    invoke-virtual {v2, v7}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_1

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v4, :cond_1

    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mTREE_ELEMENT(Z)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x30

    if-lt v2, v4, :cond_3

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x39

    if-gt v2, v4, :cond_3

    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mINT(Z)V

    :goto_0
    if-eqz p1, :cond_2

    const/16 p1, 0xb

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_1

    :cond_2
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_3
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1
.end method

.method public final mAST_ITEM(Z)V
    .locals 8

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x2

    const/16 v4, 0x23

    if-ne v2, v4, :cond_0

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v5, 0x28

    if-ne v2, v5, :cond_0

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mTREE(Z)V

    goto/16 :goto_3

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v5, 0x3d

    const/4 v6, 0x0

    if-ne v2, v4, :cond_7

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v7

    invoke-virtual {v2, v7}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_7

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x9

    if-eq v2, v3, :cond_1

    const/16 v3, 0xa

    if-eq v2, v3, :cond_1

    const/16 v3, 0xd

    if-eq v2, v3, :cond_1

    const/16 v3, 0x20

    if-eq v2, v3, :cond_1

    const/16 v3, 0x3a

    if-eq v2, v3, :cond_2

    const/16 v3, 0x5f

    if-eq v2, v3, :cond_2

    packed-switch v2, :pswitch_data_0

    packed-switch v2, :pswitch_data_1

    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_1
    invoke-virtual {p0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :cond_2
    :pswitch_0
    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mID(Z)V

    iget-object v2, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    iget-object v4, p0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    iget-object v7, p0, Lantlr/actions/cpp/ActionLexer;->transInfo:Lantlr/ActionTransInfo;

    invoke-virtual {v4, v2, v7}, Lantlr/CodeGenerator;->mapTreeId(Ljava/lang/String;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v2

    if-eqz v2, :cond_3

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-nez v4, :cond_3

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v0}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    goto/16 :goto_0

    :cond_3
    const-string v2, "if"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "define"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "ifdef"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "ifndef"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "else"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "elif"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "endif"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "warning"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "error"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "ident"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "pragma"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_4

    const-string v2, "include"

    invoke-virtual {v3, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_5

    :cond_4
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v0}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "#"

    invoke-virtual {v4, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    :cond_5
    :goto_0
    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_6

    invoke-virtual {p0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :cond_6
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v5, :cond_c

    goto/16 :goto_2

    :cond_7
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_8

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v7, 0x5b

    if-ne v2, v7, :cond_8

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mAST_CONSTRUCTOR(Z)V

    goto :goto_3

    :cond_8
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_e

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_e

    const-string v2, "##"

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/actions/cpp/ActionLexer;->currentRule:Lantlr/RuleBlock;

    if-eqz v3, :cond_9

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v3, p0, Lantlr/actions/cpp/ActionLexer;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v3}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v3, "_AST"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v0}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/actions/cpp/ActionLexer;->transInfo:Lantlr/ActionTransInfo;

    if-eqz v3, :cond_a

    iput-object v2, v3, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    goto :goto_1

    :cond_9
    const-string v3, "\"##\" not valid in this context"

    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->reportWarning(Ljava/lang/String;)V

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v0}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    :cond_a
    :goto_1
    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_b

    invoke-virtual {p0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :cond_b
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v5, :cond_c

    :goto_2
    invoke-virtual {p0, v6}, Lantlr/actions/cpp/ActionLexer;->mVAR_ASSIGN(Z)V

    :cond_c
    :goto_3
    if-eqz p1, :cond_d

    const/4 p1, 0x6

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_4

    :cond_d
    const/4 p1, 0x0

    :goto_4
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_e
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    nop

    :pswitch_data_0
    .packed-switch 0x41
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x61
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public final mCHAR(Z)V
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x27

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x5c

    if-ne v3, v4, :cond_0

    const/4 v2, 0x0

    invoke-virtual {p0, v2}, Lantlr/actions/cpp/ActionLexer;->mESC(Z)V

    goto :goto_0

    :cond_0
    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_8:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_1

    const/16 p1, 0x16

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_1

    :cond_1
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_2
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1
.end method

.method public final mCOMMENT(Z)V
    .locals 6

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x0

    const/4 v4, 0x2

    const/16 v5, 0x2f

    if-ne v2, v5, :cond_0

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_0

    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mSL_COMMENT(Z)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x2a

    if-ne v2, v4, :cond_2

    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mML_COMMENT(Z)V

    :goto_0
    if-eqz p1, :cond_1

    const/16 p1, 0x13

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_1

    :cond_1
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_2
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1
.end method

.method public final mDIGIT(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x30

    const/16 v2, 0x39

    invoke-virtual {p0, v1, v2}, Lantlr/CharScanner;->matchRange(CC)V

    if-eqz p1, :cond_0

    const/16 p1, 0x19

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mESC(Z)V
    .locals 9

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x5c

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x22

    if-eq v3, v4, :cond_7

    const/16 v4, 0x27

    if-eq v3, v4, :cond_7

    if-eq v3, v1, :cond_6

    const/16 v1, 0x62

    if-eq v3, v1, :cond_6

    const/16 v1, 0x66

    if-eq v3, v1, :cond_6

    const/16 v1, 0x6e

    if-eq v3, v1, :cond_6

    const/16 v1, 0x72

    if-eq v3, v1, :cond_6

    const/16 v1, 0x74

    if-eq v3, v1, :cond_6

    const/16 v1, 0x76

    if-eq v3, v1, :cond_6

    const/16 v1, 0x39

    const/16 v4, 0x30

    const/4 v5, 0x2

    const/16 v6, 0xff

    const/4 v7, 0x3

    packed-switch v3, :pswitch_data_0

    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :pswitch_0
    const/16 v3, 0x34

    const/16 v8, 0x37

    invoke-virtual {p0, v3, v8}, Lantlr/CharScanner;->matchRange(CC)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v4, :cond_0

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v1, :cond_0

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v7, :cond_0

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_0

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v7, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_1

    goto/16 :goto_1

    :cond_1
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :pswitch_1
    const/16 v3, 0x33

    invoke-virtual {p0, v4, v3}, Lantlr/CharScanner;->matchRange(CC)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v4, :cond_4

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v1, :cond_4

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v7, :cond_4

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_4

    const/4 v3, 0x0

    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mDIGIT(Z)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v4, :cond_2

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v1, :cond_2

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v7, :cond_2

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_2

    :goto_0
    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mDIGIT(Z)V

    goto :goto_1

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v7, :cond_3

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_3

    goto :goto_1

    :cond_3
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_4
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v7, :cond_5

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_5

    goto :goto_1

    :cond_5
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_6
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    goto :goto_1

    :cond_7
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_1
    if-eqz p1, :cond_8

    const/16 p1, 0x18

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_2

    :cond_8
    const/4 p1, 0x0

    :goto_2
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

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

.method public final mID(Z)V
    .locals 7

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x5f

    const/16 v4, 0x3a

    if-eq v2, v4, :cond_0

    if-eq v2, v3, :cond_1

    packed-switch v2, :pswitch_data_0

    packed-switch v2, :pswitch_data_1

    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_0
    :pswitch_0
    const-string v2, "::"

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    :goto_0
    const/4 v2, 0x0

    const/16 v4, 0x11

    sget-object v5, Lantlr/actions/cpp/ActionLexer;->_tokenSet_9:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    invoke-virtual {v5, v6}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v5

    if-eqz v5, :cond_2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v3, :cond_1

    packed-switch v2, :pswitch_data_2

    packed-switch v2, :pswitch_data_3

    packed-switch v2, :pswitch_data_4

    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :pswitch_1
    const/16 v2, 0x30

    const/16 v4, 0x39

    goto :goto_1

    :pswitch_2
    const/16 v2, 0x41

    const/16 v4, 0x5a

    :goto_1
    invoke-virtual {p0, v2, v4}, Lantlr/CharScanner;->matchRange(CC)V

    goto :goto_0

    :pswitch_3
    const/16 v2, 0x61

    const/16 v4, 0x7a

    goto :goto_1

    :cond_1
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    :cond_2
    if-eqz p1, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v2

    new-instance p1, Ljava/lang/String;

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v1

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {p1, v1, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {v2, p1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    :cond_3
    iput-object v2, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :pswitch_data_0
    .packed-switch 0x41
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x61
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x30
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_0
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0x41
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
    .end packed-switch

    :pswitch_data_4
    .packed-switch 0x61
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
        :pswitch_3
    .end packed-switch
.end method

.method public final mID_ELEMENT(Z)Z
    .locals 16

    move-object/from16 v0, p0

    iget-object v1, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    const/4 v2, 0x1

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->mID(Z)V

    iget-object v3, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    sget-object v4, Lantlr/actions/cpp/ActionLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v4, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v4

    const/4 v5, 0x2

    const/4 v6, 0x0

    if-eqz v4, :cond_0

    sget-object v4, Lantlr/actions/cpp/ActionLexer;->_tokenSet_13:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v7

    invoke-virtual {v4, v7}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v4

    if-eqz v4, :cond_0

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v4

    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v7, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v7, v4}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_0

    :cond_0
    sget-object v4, Lantlr/actions/cpp/ActionLexer;->_tokenSet_13:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v7

    invoke-virtual {v4, v7}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v4

    if-eqz v4, :cond_1d

    :goto_0
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    const/16 v9, 0x5f

    const/16 v10, 0x3e

    const/16 v11, 0x3c

    const/4 v12, 0x3

    const/16 v13, 0x28

    const/16 v14, 0x20

    const/16 v15, 0xd

    const/16 v7, 0xa

    const/16 v8, 0x9

    if-eq v4, v13, :cond_11

    const/16 v13, 0x2e

    if-eq v4, v13, :cond_10

    const/16 v13, 0x3a

    if-eq v4, v13, :cond_f

    if-eq v4, v11, :cond_11

    const/16 v11, 0x5b

    if-eq v4, v11, :cond_8

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    const/16 v9, 0x2d

    if-ne v4, v9, :cond_1

    invoke-virtual {v0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    if-ne v4, v10, :cond_1

    sget-object v4, Lantlr/actions/cpp/ActionLexer;->_tokenSet_12:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v12}, Lantlr/CharScanner;->LA(I)C

    move-result v9

    invoke-virtual {v4, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v4

    if-eqz v4, :cond_1

    const-string v2, "->"

    goto/16 :goto_4

    :cond_1
    sget-object v4, Lantlr/actions/cpp/ActionLexer;->_tokenSet_16:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v9

    invoke-virtual {v4, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v4

    if-eqz v4, :cond_7

    iget-object v4, v0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    iget-object v9, v0, Lantlr/actions/cpp/ActionLexer;->transInfo:Lantlr/ActionTransInfo;

    invoke-virtual {v4, v3, v9}, Lantlr/CodeGenerator;->mapTreeId(Ljava/lang/String;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v3

    if-eqz v3, :cond_2

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    :cond_2
    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_17:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_5

    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_16:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_5

    iget-object v3, v0, Lantlr/actions/cpp/ActionLexer;->transInfo:Lantlr/ActionTransInfo;

    if-eqz v3, :cond_5

    iget-object v3, v3, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    if-eqz v3, :cond_5

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_4

    if-eq v3, v7, :cond_4

    if-eq v3, v15, :cond_4

    if-eq v3, v14, :cond_4

    const/16 v4, 0x3d

    if-ne v3, v4, :cond_3

    goto :goto_1

    :cond_3
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_4
    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :goto_1
    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mVAR_ASSIGN(Z)V

    goto/16 :goto_c

    :cond_5
    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_18:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_6

    goto/16 :goto_c

    :cond_6
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_7
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_8
    move v3, v6

    :goto_2
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    if-ne v4, v11, :cond_d

    invoke-virtual {v0, v11}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    if-eq v4, v8, :cond_9

    if-eq v4, v7, :cond_9

    if-eq v4, v15, :cond_9

    if-eq v4, v14, :cond_9

    if-eq v4, v9, :cond_a

    const/16 v5, 0x22

    if-eq v4, v5, :cond_a

    const/16 v5, 0x23

    if-eq v4, v5, :cond_a

    const/16 v5, 0x27

    if-eq v4, v5, :cond_a

    const/16 v5, 0x28

    if-eq v4, v5, :cond_a

    packed-switch v4, :pswitch_data_0

    packed-switch v4, :pswitch_data_1

    packed-switch v4, :pswitch_data_2

    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_9
    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v4

    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5, v4}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_a
    :pswitch_0
    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mARG(Z)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    if-eq v4, v8, :cond_c

    if-eq v4, v7, :cond_c

    if-eq v4, v15, :cond_c

    if-eq v4, v14, :cond_c

    const/16 v5, 0x5d

    if-ne v4, v5, :cond_b

    goto :goto_3

    :cond_b
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_c
    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v4

    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5, v4}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_3
    const/16 v4, 0x5d

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->match(C)V

    add-int/lit8 v3, v3, 0x1

    goto/16 :goto_2

    :cond_d
    if-lt v3, v2, :cond_e

    goto/16 :goto_b

    :cond_e
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_f
    const-string v2, "::"

    :goto_4
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    goto :goto_5

    :cond_10
    const/16 v2, 0x2e

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->match(C)V

    :goto_5
    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mID_ELEMENT(Z)Z

    goto/16 :goto_b

    :cond_11
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x28

    if-eq v3, v4, :cond_14

    if-ne v3, v11, :cond_13

    invoke-virtual {v0, v11}, Lantlr/CharScanner;->match(C)V

    :goto_6
    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_14:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_12

    invoke-virtual {v0, v10}, Lantlr/CharScanner;->matchNot(C)V

    goto :goto_6

    :cond_12
    invoke-virtual {v0, v10}, Lantlr/CharScanner;->match(C)V

    const/16 v3, 0x28

    goto :goto_7

    :cond_13
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_14
    move v3, v4

    :goto_7
    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(C)V

    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_15

    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_15:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_15

    invoke-virtual {v0, v12}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v12, :cond_15

    invoke-virtual {v0, v12}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0xff

    if-gt v3, v4, :cond_15

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_8

    :cond_15
    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_15:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_1c

    invoke-virtual {v0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v12, :cond_1c

    invoke-virtual {v0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0xff

    if-gt v3, v4, :cond_1c

    :goto_8
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_18

    if-eq v3, v7, :cond_18

    if-eq v3, v15, :cond_18

    if-eq v3, v14, :cond_18

    if-eq v3, v9, :cond_16

    const/16 v4, 0x22

    if-eq v3, v4, :cond_16

    const/16 v4, 0x23

    if-eq v3, v4, :cond_16

    packed-switch v3, :pswitch_data_3

    packed-switch v3, :pswitch_data_4

    packed-switch v3, :pswitch_data_5

    packed-switch v3, :pswitch_data_6

    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_16
    :goto_9
    :pswitch_1
    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mARG(Z)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x2c

    if-ne v3, v4, :cond_18

    const/16 v3, 0x2c

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_17

    if-eq v3, v7, :cond_17

    if-eq v3, v15, :cond_17

    if-eq v3, v14, :cond_17

    if-eq v3, v9, :cond_16

    const/16 v4, 0x22

    if-eq v3, v4, :cond_16

    const/16 v5, 0x23

    if-eq v3, v5, :cond_16

    const/16 v10, 0x27

    if-eq v3, v10, :cond_16

    const/16 v10, 0x28

    if-eq v3, v10, :cond_16

    packed-switch v3, :pswitch_data_7

    packed-switch v3, :pswitch_data_8

    packed-switch v3, :pswitch_data_9

    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_17
    const/16 v4, 0x22

    const/16 v5, 0x23

    const/16 v10, 0x28

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v11, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_9

    :cond_18
    :pswitch_2
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_1a

    if-eq v3, v7, :cond_1a

    if-eq v3, v15, :cond_1a

    if-eq v3, v14, :cond_1a

    const/16 v4, 0x29

    if-ne v3, v4, :cond_19

    goto :goto_a

    :cond_19
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_1a
    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {v0, v6}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_a
    const/16 v2, 0x29

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->match(C)V

    :goto_b
    move v2, v6

    :goto_c
    if-eqz p1, :cond_1b

    const/16 v3, 0xc

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v3

    new-instance v4, Ljava/lang/String;

    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v5

    iget-object v6, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v6}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v6

    sub-int/2addr v6, v1

    invoke-direct {v4, v5, v1, v6}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {v3, v4}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_d

    :cond_1b
    const/4 v3, 0x0

    :goto_d
    iput-object v3, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return v2

    :cond_1c
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_1d
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :pswitch_data_0
    .packed-switch 0x30
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x41
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x61
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0x27
        :pswitch_1
        :pswitch_1
        :pswitch_2
    .end packed-switch

    :pswitch_data_4
    .packed-switch 0x30
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_5
    .packed-switch 0x41
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_6
    .packed-switch 0x61
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_7
    .packed-switch 0x30
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_8
    .packed-switch 0x41
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_9
    .packed-switch 0x61
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch
.end method

.method public final mINT(Z)V
    .locals 6

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    const/4 v3, 0x1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    const/16 v5, 0x30

    if-lt v4, v5, :cond_0

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    const/16 v5, 0x39

    if-gt v4, v5, :cond_0

    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mDIGIT(Z)V

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_0
    if-lt v2, v3, :cond_2

    if-eqz p1, :cond_1

    const/16 p1, 0x1a

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_1

    :cond_1
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_2
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1
.end method

.method public final mINT_OR_FLOAT(Z)V
    .locals 9

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    const/4 v3, 0x1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    const/16 v5, 0x39

    const/16 v6, 0x30

    const/4 v7, 0x2

    if-lt v4, v6, :cond_0

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    if-gt v4, v5, :cond_0

    sget-object v4, Lantlr/actions/cpp/ActionLexer;->_tokenSet_25:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    invoke-virtual {v4, v8}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v4

    if-eqz v4, :cond_0

    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mDIGIT(Z)V

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_0
    if-lt v2, v3, :cond_7

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x4c

    if-ne v2, v4, :cond_1

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_26:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    invoke-virtual {v2, v8}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1

    :goto_1
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    goto :goto_3

    :cond_1
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x6c

    if-ne v2, v4, :cond_2

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_26:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    invoke-virtual {v2, v8}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_2

    goto :goto_1

    :cond_2
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x2e

    if-ne v2, v4, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_2
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_4

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v5, :cond_4

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_26:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v2, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_4

    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mDIGIT(Z)V

    goto :goto_2

    :cond_3
    sget-object v1, Lantlr/actions/cpp/ActionLexer;->_tokenSet_26:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual {v1, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_6

    :cond_4
    :goto_3
    if-eqz p1, :cond_5

    const/16 p1, 0x1b

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_4

    :cond_5
    const/4 p1, 0x0

    :goto_4
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_6
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_7
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1
.end method

.method public final mML_COMMENT(Z)V
    .locals 8

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const-string v1, "/*"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x2a

    const/4 v4, 0x2

    if-ne v2, v3, :cond_0

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x2f

    if-ne v2, v3, :cond_0

    goto/16 :goto_3

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xa

    const/16 v5, 0xd

    const/16 v6, 0xff

    const/4 v7, 0x3

    if-ne v2, v5, :cond_1

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v3, :cond_1

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_1

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_1

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    :goto_1
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    :goto_2
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_2

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_2

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_2

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v3, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_3

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_3

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_3

    goto :goto_1

    :cond_3
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_4

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_4

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v7, :cond_4

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_4

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v7, :cond_4

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_4

    const v1, 0xffff

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    goto/16 :goto_0

    :cond_4
    :goto_3
    const-string v1, "*/"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    if-eqz p1, :cond_5

    const/16 p1, 0x15

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_4

    :cond_5
    const/4 p1, 0x0

    :goto_4
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mSL_COMMENT(Z)V
    .locals 8

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const-string v1, "//"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x2

    const/16 v4, 0xd

    const/16 v5, 0xa

    if-eq v2, v5, :cond_1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_0

    goto :goto_1

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v6, 0x3

    if-lt v2, v6, :cond_1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v7, 0xff

    if-gt v2, v7, :cond_1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v7, :cond_1

    const v1, 0xffff

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    goto :goto_0

    :cond_1
    :goto_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_2

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_2

    const-string v1, "\r\n"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    goto :goto_2

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_3

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_3
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_5

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_2
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    if-eqz p1, :cond_4

    const/16 p1, 0x14

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_3

    :cond_4
    const/4 p1, 0x0

    :goto_3
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_5
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1
.end method

.method public final mSTRING(Z)V
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x22

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :goto_0
    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x5c

    if-ne v3, v4, :cond_0

    const/4 v2, 0x0

    invoke-virtual {p0, v2}, Lantlr/actions/cpp/ActionLexer;->mESC(Z)V

    goto :goto_0

    :cond_0
    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_7:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual {v3, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_2

    const/16 p1, 0x17

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_1

    :cond_2
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mSTUFF(Z)V
    .locals 8

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xa

    if-eq v2, v3, :cond_9

    const/16 v4, 0x22

    const/4 v5, 0x0

    if-eq v2, v4, :cond_8

    const/16 v4, 0x27

    if-eq v2, v4, :cond_7

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x2f

    const/4 v6, 0x2

    if-ne v2, v4, :cond_1

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v7, 0x2a

    if-eq v2, v7, :cond_0

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_1

    :cond_0
    invoke-virtual {p0, v5}, Lantlr/actions/cpp/ActionLexer;->mCOMMENT(Z)V

    goto/16 :goto_2

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v5, 0xd

    if-ne v2, v5, :cond_2

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v3, :cond_2

    const-string v1, "\r\n"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    goto/16 :goto_1

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x5c

    if-ne v2, v3, :cond_3

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v7, 0x23

    if-ne v2, v7, :cond_3

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(C)V

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1, v0}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    const-string v2, "#"

    invoke-virtual {v1, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    goto :goto_2

    :cond_3
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_4

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_4

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    sget-object v1, Lantlr/actions/cpp/ActionLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Lantlr/collections/impl/BitSet;)V

    goto :goto_2

    :cond_4
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_5

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    goto :goto_1

    :cond_5
    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_6

    sget-object v1, Lantlr/actions/cpp/ActionLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    goto :goto_0

    :cond_6
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_7
    invoke-virtual {p0, v5}, Lantlr/actions/cpp/ActionLexer;->mCHAR(Z)V

    goto :goto_2

    :cond_8
    invoke-virtual {p0, v5}, Lantlr/actions/cpp/ActionLexer;->mSTRING(Z)V

    goto :goto_2

    :cond_9
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    :goto_1
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    :goto_2
    if-eqz p1, :cond_a

    const/4 p1, 0x5

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_3

    :cond_a
    const/4 p1, 0x0

    :goto_3
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mTEXT_ARG(Z)V
    .locals 8

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xd

    const/4 v4, 0x0

    const/16 v5, 0x9

    if-eq v2, v5, :cond_0

    const/16 v5, 0xa

    if-eq v2, v5, :cond_0

    if-eq v2, v3, :cond_0

    const/16 v5, 0x20

    if-eq v2, v5, :cond_0

    const/16 v5, 0x22

    if-eq v2, v5, :cond_1

    const/16 v5, 0x24

    if-eq v2, v5, :cond_1

    const/16 v5, 0x27

    if-eq v2, v5, :cond_1

    const/16 v5, 0x2b

    if-eq v2, v5, :cond_1

    const/16 v5, 0x5f

    if-eq v2, v5, :cond_1

    packed-switch v2, :pswitch_data_0

    packed-switch v2, :pswitch_data_1

    packed-switch v2, :pswitch_data_2

    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_0
    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :cond_1
    :pswitch_0
    move v2, v4

    :goto_0
    sget-object v5, Lantlr/actions/cpp/ActionLexer;->_tokenSet_10:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    invoke-virtual {v5, v6}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v5

    if-eqz v5, :cond_4

    const/4 v5, 0x2

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    const/4 v7, 0x3

    if-lt v6, v7, :cond_4

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    const/16 v7, 0xff

    if-gt v6, v7, :cond_4

    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG_ELEMENT(Z)V

    sget-object v6, Lantlr/actions/cpp/ActionLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v7

    invoke-virtual {v6, v7}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v6

    if-eqz v6, :cond_2

    sget-object v6, Lantlr/actions/cpp/ActionLexer;->_tokenSet_11:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v6, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v5

    if-eqz v5, :cond_2

    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    goto :goto_1

    :cond_2
    sget-object v5, Lantlr/actions/cpp/ActionLexer;->_tokenSet_11:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    invoke-virtual {v5, v6}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v5

    if-eqz v5, :cond_3

    :goto_1
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_3
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_4
    if-lt v2, v1, :cond_6

    if-eqz p1, :cond_5

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_2

    :cond_5
    const/4 p1, 0x0

    :goto_2
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_6
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    nop

    :pswitch_data_0
    .packed-switch 0x30
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x41
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x61
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public final mTEXT_ARG_ELEMENT(Z)V
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x0

    const/16 v4, 0x22

    if-eq v2, v4, :cond_4

    const/16 v4, 0x24

    if-eq v2, v4, :cond_3

    const/16 v4, 0x27

    if-eq v2, v4, :cond_2

    const/16 v4, 0x2b

    if-eq v2, v4, :cond_1

    const/16 v4, 0x5f

    if-eq v2, v4, :cond_0

    packed-switch v2, :pswitch_data_0

    packed-switch v2, :pswitch_data_1

    packed-switch v2, :pswitch_data_2

    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :pswitch_0
    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mINT_OR_FLOAT(Z)V

    goto :goto_0

    :cond_0
    :pswitch_1
    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG_ID_ELEMENT(Z)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mCHAR(Z)V

    goto :goto_0

    :cond_3
    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ITEM(Z)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v3}, Lantlr/actions/cpp/ActionLexer;->mSTRING(Z)V

    :goto_0
    if-eqz p1, :cond_5

    const/16 p1, 0xe

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_1

    :cond_5
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :pswitch_data_0
    .packed-switch 0x30
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_1
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x41
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x61
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch
.end method

.method public final mTEXT_ARG_ID_ELEMENT(Z)V
    .locals 14

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mID(Z)V

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    const/4 v3, 0x2

    const/4 v4, 0x0

    if-eqz v2, :cond_0

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_22:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v2, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_0

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v5, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_0

    :cond_0
    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_22:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v2, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_14

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v5, 0x20

    const/16 v6, 0xd

    const/16 v7, 0xa

    const/16 v8, 0x9

    const/16 v9, 0x28

    const/16 v10, 0xff

    const/4 v11, 0x3

    if-eq v2, v9, :cond_c

    const/16 v9, 0x5b

    if-eq v2, v9, :cond_5

    const/16 v5, 0x2d

    if-eq v2, v5, :cond_4

    const/16 v5, 0x2e

    if-eq v2, v5, :cond_3

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v5, 0x3a

    if-ne v2, v5, :cond_1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_1

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_12:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_1

    const-string v1, "::"

    goto :goto_1

    :cond_1
    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_11:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_2

    goto/16 :goto_9

    :cond_2
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_3
    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_4
    const-string v1, "->"

    :goto_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    :goto_2
    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG_ID_ELEMENT(Z)V

    goto/16 :goto_9

    :cond_5
    move v2, v4

    :goto_3
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v12

    if-ne v12, v9, :cond_a

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    sget-object v12, Lantlr/actions/cpp/ActionLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v13

    invoke-virtual {v12, v13}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v12

    if-eqz v12, :cond_6

    sget-object v12, Lantlr/actions/cpp/ActionLexer;->_tokenSet_24:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v13

    invoke-virtual {v12, v13}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v12

    if-eqz v12, :cond_6

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->LA(I)C

    move-result v12

    if-lt v12, v11, :cond_6

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->LA(I)C

    move-result v12

    if-gt v12, v10, :cond_6

    iget-object v12, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v12}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v12

    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v13, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v13, v12}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_4

    :cond_6
    sget-object v12, Lantlr/actions/cpp/ActionLexer;->_tokenSet_24:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v13

    invoke-virtual {v12, v13}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v12

    if-eqz v12, :cond_9

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v12

    if-lt v12, v11, :cond_9

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v12

    if-gt v12, v10, :cond_9

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->LA(I)C

    move-result v12

    if-lt v12, v11, :cond_9

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->LA(I)C

    move-result v12

    if-gt v12, v10, :cond_9

    :goto_4
    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG(Z)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v12

    if-eq v12, v8, :cond_8

    if-eq v12, v7, :cond_8

    if-eq v12, v6, :cond_8

    if-eq v12, v5, :cond_8

    const/16 v13, 0x5d

    if-ne v12, v13, :cond_7

    goto :goto_5

    :cond_7
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_8
    iget-object v12, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v12}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v12

    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v13, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v13, v12}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_5
    const/16 v12, 0x5d

    invoke-virtual {p0, v12}, Lantlr/CharScanner;->match(C)V

    add-int/lit8 v2, v2, 0x1

    goto/16 :goto_3

    :cond_9
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_a
    if-lt v2, v1, :cond_b

    goto/16 :goto_9

    :cond_b
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_c
    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v9

    invoke-virtual {v2, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_d

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_23:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v9

    invoke-virtual {v2, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_d

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v11, :cond_d

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v10, :cond_d

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v9, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v9, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_6

    :cond_d
    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_23:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v9

    invoke-virtual {v2, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_13

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v11, :cond_13

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v10, :cond_13

    :cond_e
    :goto_6
    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_24:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v9

    invoke-virtual {v2, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_f

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v11, :cond_f

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v10, :cond_f

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v11, :cond_f

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v10, :cond_f

    :goto_7
    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG(Z)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v9, 0x2c

    if-ne v2, v9, :cond_e

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    goto :goto_7

    :cond_f
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x29

    if-eq v2, v8, :cond_11

    if-eq v2, v7, :cond_11

    if-eq v2, v6, :cond_11

    if-eq v2, v5, :cond_11

    if-ne v2, v3, :cond_10

    goto :goto_8

    :cond_10
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_11
    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_8
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    :goto_9
    if-eqz p1, :cond_12

    const/16 p1, 0xf

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_a

    :cond_12
    const/4 p1, 0x0

    :goto_a
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_13
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_14
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1
.end method

.method public final mTEXT_ITEM(Z)V
    .locals 16

    move-object/from16 v0, p0

    iget-object v1, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    const/4 v2, 0x1

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const-string v4, "): unknown rule or bad lookahead computation"

    const/16 v5, 0x46

    const/16 v7, 0x24

    const/16 v8, 0x20

    const/16 v9, 0xd

    const/16 v10, 0x29

    const/16 v11, 0xa

    const/4 v12, 0x0

    const/16 v13, 0x9

    const/4 v14, 0x3

    const/4 v15, 0x2

    const/16 v6, 0x28

    if-ne v3, v7, :cond_4

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v5, :cond_4

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0x4f

    if-ne v3, v5, :cond_4

    const-string v3, "$FOLLOW"

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v3, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_2

    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v3, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v14, :cond_2

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0xff

    if-gt v3, v5, :cond_2

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v13, :cond_1

    if-eq v3, v11, :cond_1

    if-eq v3, v9, :cond_1

    if-eq v3, v8, :cond_1

    if-ne v3, v6, :cond_0

    goto :goto_0

    :cond_0
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_1
    invoke-virtual {v0, v12}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :goto_0
    invoke-virtual {v0, v6}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG(Z)V

    iget-object v6, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0, v10}, Lantlr/CharScanner;->match(C)V

    goto :goto_1

    :cond_2
    const/4 v6, 0x0

    :goto_1
    iget-object v3, v0, Lantlr/actions/cpp/ActionLexer;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v3}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v3

    if-eqz v6, :cond_3

    invoke-virtual {v6}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    :cond_3
    iget-object v5, v0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v5, v3, v2}, Lantlr/CodeGenerator;->getFOLLOWBitSet(Ljava/lang/String;I)Ljava/lang/String;

    move-result-object v2

    if-nez v2, :cond_c

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "$FOLLOW("

    :goto_2
    invoke-virtual {v2, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->reportError(Ljava/lang/String;)V

    goto/16 :goto_b

    :cond_4
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v7, :cond_9

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0x46

    if-ne v3, v5, :cond_9

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0x49

    if-ne v3, v5, :cond_9

    const-string v3, "$FIRST"

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v3, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_7

    sget-object v3, Lantlr/actions/cpp/ActionLexer;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v3, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_7

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v14, :cond_7

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0xff

    if-gt v3, v5, :cond_7

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v13, :cond_6

    if-eq v3, v11, :cond_6

    if-eq v3, v9, :cond_6

    if-eq v3, v8, :cond_6

    if-ne v3, v6, :cond_5

    goto :goto_3

    :cond_5
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_6
    invoke-virtual {v0, v12}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :goto_3
    invoke-virtual {v0, v6}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG(Z)V

    iget-object v6, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0, v10}, Lantlr/CharScanner;->match(C)V

    goto :goto_4

    :cond_7
    const/4 v6, 0x0

    :goto_4
    iget-object v3, v0, Lantlr/actions/cpp/ActionLexer;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v3}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v3

    if-eqz v6, :cond_8

    invoke-virtual {v6}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    :cond_8
    iget-object v5, v0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v5, v3, v2}, Lantlr/CodeGenerator;->getFIRSTBitSet(Ljava/lang/String;I)Ljava/lang/String;

    move-result-object v2

    if-nez v2, :cond_c

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "$FIRST("

    goto/16 :goto_2

    :cond_9
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v7, :cond_d

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x61

    if-ne v3, v4, :cond_d

    const-string v3, "$append"

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v13, :cond_b

    if-eq v3, v11, :cond_b

    if-eq v3, v9, :cond_b

    if-eq v3, v8, :cond_b

    if-ne v3, v6, :cond_a

    goto :goto_5

    :cond_a
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_b
    invoke-virtual {v0, v12}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :goto_5
    invoke-virtual {v0, v6}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG(Z)V

    iget-object v2, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0, v10}, Lantlr/CharScanner;->match(C)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "text += "

    :goto_6
    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v2, v3}, La/a/a/a/a;->a(Lantlr/Token;Ljava/lang/StringBuilder;)Ljava/lang/String;

    move-result-object v2

    :cond_c
    :goto_7
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    goto/16 :goto_b

    :cond_d
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v7, :cond_17

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x73

    if-ne v3, v4, :cond_17

    const-string v3, "$set"

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x54

    if-ne v3, v4, :cond_10

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0x65

    if-ne v3, v5, :cond_10

    const-string v3, "Text"

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v13, :cond_f

    if-eq v3, v11, :cond_f

    if-eq v3, v9, :cond_f

    if-eq v3, v8, :cond_f

    if-ne v3, v6, :cond_e

    goto :goto_8

    :cond_e
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_f
    invoke-virtual {v0, v12}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :goto_8
    invoke-virtual {v0, v6}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG(Z)V

    iget-object v2, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0, v10}, Lantlr/CharScanner;->match(C)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "{ text.erase(_begin); text += "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "; }"

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    goto/16 :goto_7

    :cond_10
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v4, :cond_13

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0x6f

    if-ne v3, v5, :cond_13

    const-string v3, "Token"

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v13, :cond_12

    if-eq v3, v11, :cond_12

    if-eq v3, v9, :cond_12

    if-eq v3, v8, :cond_12

    if-ne v3, v6, :cond_11

    goto :goto_9

    :cond_11
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_12
    invoke-virtual {v0, v12}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :goto_9
    invoke-virtual {v0, v6}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG(Z)V

    iget-object v2, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0, v10}, Lantlr/CharScanner;->match(C)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "_token = "

    goto/16 :goto_6

    :cond_13
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v4, :cond_16

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x79

    if-ne v3, v4, :cond_16

    const-string v3, "Type"

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v13, :cond_15

    if-eq v3, v11, :cond_15

    if-eq v3, v9, :cond_15

    if-eq v3, v8, :cond_15

    if-ne v3, v6, :cond_14

    goto :goto_a

    :cond_14
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_15
    invoke-virtual {v0, v12}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    :goto_a
    invoke-virtual {v0, v6}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {v0, v2}, Lantlr/actions/cpp/ActionLexer;->mTEXT_ARG(Z)V

    iget-object v2, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0, v10}, Lantlr/CharScanner;->match(C)V

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "_ttype = "

    goto/16 :goto_6

    :cond_16
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_17
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v7, :cond_19

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x67

    if-ne v3, v4, :cond_19

    const-string v2, "$getText"

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    const-string v3, "text.substr(_begin,text.length()-_begin)"

    invoke-virtual {v2, v3}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    :goto_b
    if-eqz p1, :cond_18

    const/4 v2, 0x7

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v6

    new-instance v2, Ljava/lang/String;

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v3

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v4

    sub-int/2addr v4, v1

    invoke-direct {v2, v3, v1, v4}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {v6, v2}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_c

    :cond_18
    const/4 v6, 0x0

    :goto_c
    iput-object v6, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_19
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1
.end method

.method public final mTREE(Z)V
    .locals 16

    move-object/from16 v0, p0

    iget-object v1, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    new-instance v2, Lantlr/collections/impl/Vector;

    const/16 v3, 0xa

    invoke-direct {v2, v3}, Lantlr/collections/impl/Vector;-><init>(I)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v4

    const/16 v5, 0x28

    invoke-virtual {v0, v5}, Lantlr/CharScanner;->match(C)V

    iget-object v6, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v6, v4}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    const/4 v4, 0x1

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    const/16 v7, 0x23

    const/16 v8, 0x22

    const/16 v9, 0x5f

    const/16 v10, 0x3a

    const/4 v11, 0x0

    const/16 v12, 0x20

    const/16 v13, 0xd

    const/16 v14, 0x9

    if-eq v6, v14, :cond_0

    if-eq v6, v3, :cond_0

    if-eq v6, v13, :cond_0

    if-eq v6, v12, :cond_0

    if-eq v6, v5, :cond_1

    if-eq v6, v10, :cond_1

    if-eq v6, v9, :cond_1

    if-eq v6, v8, :cond_1

    if-eq v6, v7, :cond_1

    packed-switch v6, :pswitch_data_0

    packed-switch v6, :pswitch_data_1

    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_0
    iget-object v6, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v6}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v6

    invoke-virtual {v0, v11}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v6}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_1
    :pswitch_0
    iget-object v6, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v6}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v6

    invoke-virtual {v0, v4}, Lantlr/actions/cpp/ActionLexer;->mTREE_ELEMENT(Z)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v6}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v6, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    iget-object v15, v0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v6}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v15, v6}, Lantlr/CodeGenerator;->processStringForASTConstructor(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v2, v6}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    const/16 v15, 0x29

    const/16 v7, 0x2c

    if-eq v6, v14, :cond_4

    if-eq v6, v3, :cond_4

    if-eq v6, v13, :cond_4

    if-eq v6, v12, :cond_4

    if-eq v6, v15, :cond_3

    if-ne v6, v7, :cond_2

    goto :goto_0

    :cond_2
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual/range {p0 .. p0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_3
    :goto_0
    move v6, v1

    move/from16 v1, p1

    goto :goto_2

    :cond_4
    move v6, v1

    move/from16 v1, p1

    :goto_1
    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v15

    invoke-virtual {v0, v11}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v11, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v15}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_2
    const/16 v15, 0x8

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v11

    if-ne v11, v7, :cond_b

    iget-object v11, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v11

    invoke-virtual {v0, v7}, Lantlr/CharScanner;->match(C)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v11}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v11

    if-eq v11, v14, :cond_6

    if-eq v11, v3, :cond_6

    if-eq v11, v13, :cond_6

    if-eq v11, v12, :cond_6

    if-eq v11, v5, :cond_5

    if-eq v11, v10, :cond_5

    if-eq v11, v9, :cond_5

    if-eq v11, v8, :cond_5

    const/16 v15, 0x23

    if-eq v11, v15, :cond_7

    packed-switch v11, :pswitch_data_2

    packed-switch v11, :pswitch_data_3

    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual {v0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual {v0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_5
    const/16 v15, 0x23

    goto :goto_3

    :cond_6
    const/16 v15, 0x23

    iget-object v11, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v11

    const/4 v5, 0x0

    invoke-virtual {v0, v5}, Lantlr/actions/cpp/ActionLexer;->mWS(Z)V

    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5, v11}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_7
    :goto_3
    :pswitch_1
    iget-object v5, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v5

    invoke-virtual {v0, v4}, Lantlr/actions/cpp/ActionLexer;->mTREE_ELEMENT(Z)V

    iget-object v11, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v5}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v5, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    iget-object v11, v0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v5}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v11, v5}, Lantlr/CodeGenerator;->processStringForASTConstructor(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v2, v5}, Lantlr/collections/impl/Vector;->appendElement(Ljava/lang/Object;)V

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    if-eq v5, v14, :cond_a

    if-eq v5, v3, :cond_a

    if-eq v5, v13, :cond_a

    if-eq v5, v12, :cond_a

    const/16 v11, 0x29

    if-eq v5, v11, :cond_9

    if-ne v5, v7, :cond_8

    goto :goto_4

    :cond_8
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual {v0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0}, Lantlr/CharScanner;->getLine()I

    move-result v4

    invoke-virtual {v0}, Lantlr/CharScanner;->getColumn()I

    move-result v0

    invoke-direct {v1, v2, v3, v4, v0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1

    :cond_9
    :goto_4
    const/16 v5, 0x28

    goto/16 :goto_2

    :cond_a
    const/16 v5, 0x28

    const/4 v11, 0x0

    goto/16 :goto_1

    :cond_b
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v6}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    iget-object v4, v0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v4, v2}, Lantlr/CodeGenerator;->getASTCreateString(Lantlr/collections/impl/Vector;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const/16 v3, 0x29

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(C)V

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    if-eqz v1, :cond_c

    invoke-virtual {v0, v15}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v11

    new-instance v1, Ljava/lang/String;

    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v6

    invoke-direct {v1, v2, v6, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {v11, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_5

    :cond_c
    const/4 v11, 0x0

    :goto_5
    iput-object v11, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x41
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x61
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x41
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0x61
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch
.end method

.method public final mTREE_ELEMENT(Z)V
    .locals 8

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x0

    const/4 v4, 0x0

    const/16 v5, 0x22

    if-eq v2, v5, :cond_7

    const/16 v5, 0x28

    if-eq v2, v5, :cond_6

    const/16 v6, 0x3a

    if-eq v2, v6, :cond_5

    const/16 v6, 0x5f

    if-eq v2, v6, :cond_5

    packed-switch v2, :pswitch_data_0

    packed-switch v2, :pswitch_data_1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v6, 0x2

    const/16 v7, 0x23

    if-ne v2, v7, :cond_0

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_0

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(C)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto/16 :goto_2

    :goto_0
    :pswitch_0
    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mAST_CONSTRUCTOR(Z)V

    goto/16 :goto_3

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v7, :cond_1

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v5, 0x5b

    if-ne v2, v5, :cond_1

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(C)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v7, :cond_2

    sget-object v2, Lantlr/actions/cpp/ActionLexer;->_tokenSet_12:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v2, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_2

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(C)V

    iget-object v4, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/actions/cpp/ActionLexer;->mID_ELEMENT(Z)Z

    move-result v1

    iget-object v2, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    if-nez v1, :cond_8

    iget-object v1, p0, Lantlr/actions/cpp/ActionLexer;->generator:Lantlr/CodeGenerator;

    invoke-virtual {v2}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2, v3}, Lantlr/CodeGenerator;->mapTreeId(Ljava/lang/String;Lantlr/ActionTransInfo;)Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_8

    goto :goto_1

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v7, :cond_4

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v7, :cond_4

    const-string v1, "##"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/actions/cpp/ActionLexer;->currentRule:Lantlr/RuleBlock;

    if-eqz v2, :cond_3

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v2, p0, Lantlr/actions/cpp/ActionLexer;->currentRule:Lantlr/RuleBlock;

    invoke-virtual {v2}, Lantlr/RuleBlock;->getRuleName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "_AST"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    goto :goto_1

    :cond_3
    const-string v2, "\"##\" not valid in this context"

    invoke-virtual {p0, v2}, Lantlr/actions/cpp/ActionLexer;->reportError(Ljava/lang/String;)V

    :goto_1
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v0}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    goto :goto_3

    :cond_4
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1

    :cond_5
    :pswitch_1
    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mID_ELEMENT(Z)Z

    goto :goto_3

    :cond_6
    :goto_2
    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mTREE(Z)V

    goto :goto_3

    :cond_7
    invoke-virtual {p0, v4}, Lantlr/actions/cpp/ActionLexer;->mSTRING(Z)V

    :cond_8
    :goto_3
    if-eqz p1, :cond_9

    const/16 p1, 0x9

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v3

    new-instance p1, Ljava/lang/String;

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v1

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    sub-int/2addr v2, v0

    invoke-direct {p1, v1, v0, v2}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {v3, p1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    :cond_9
    iput-object v3, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x41
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
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
    .packed-switch 0x61
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch
.end method

.method public final mVAR_ASSIGN(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3d

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v1, :cond_0

    iget-object v1, p0, Lantlr/actions/cpp/ActionLexer;->transInfo:Lantlr/ActionTransInfo;

    if-eqz v1, :cond_0

    iget-object v3, v1, Lantlr/ActionTransInfo;->refRuleRoot:Ljava/lang/String;

    if-eqz v3, :cond_0

    iput-boolean v2, v1, Lantlr/ActionTransInfo;->assignToRoot:Z

    :cond_0
    if-eqz p1, :cond_1

    const/16 p1, 0x12

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_0

    :cond_1
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mWS(Z)V
    .locals 7

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x0

    :goto_0
    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0xa

    const/16 v5, 0xd

    if-ne v3, v5, :cond_0

    const/4 v3, 0x2

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v4, :cond_0

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    :goto_1
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_2
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto :goto_4

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v6, 0x20

    if-ne v3, v6, :cond_1

    :goto_3
    invoke-virtual {p0, v6}, Lantlr/CharScanner;->match(C)V

    goto :goto_4

    :cond_1
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v6, 0x9

    if-ne v3, v6, :cond_2

    goto :goto_3

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v5, :cond_3

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_3
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v4, :cond_4

    goto :goto_1

    :goto_4
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_4
    if-lt v1, v2, :cond_6

    if-eqz p1, :cond_5

    const/16 p1, 0x1c

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {v1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_5

    :cond_5
    const/4 p1, 0x0

    :goto_5
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_6
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {p1, v0, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw p1
.end method

.method public nextToken()Lantlr/Token;
    .locals 4

    :goto_0
    invoke-virtual {p0}, Lantlr/CharScanner;->resetText()V

    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/4 v2, 0x3

    if-lt v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0xff

    if-gt v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/actions/cpp/ActionLexer;->mACTION(Z)V

    goto :goto_1

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const v2, 0xffff

    if-ne v1, v2, :cond_2

    invoke-virtual {p0}, Lantlr/CharScanner;->uponEOF()V

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v0

    iput-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    :goto_1
    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    if-nez v0, :cond_1

    goto :goto_0

    :cond_1
    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getType()I

    move-result v0

    iget-object v1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v1, v0}, Lantlr/Token;->setType(I)V

    iget-object p0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-object p0

    :cond_2
    new-instance v1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v3

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {v1, v0, v2, v3, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v1
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Lantlr/CharStreamException; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    move-exception p0

    goto :goto_2

    :catch_1
    move-exception p0

    :try_start_1
    new-instance v0, Lantlr/TokenStreamRecognitionException;

    invoke-direct {v0, p0}, Lantlr/TokenStreamRecognitionException;-><init>(Lantlr/RecognitionException;)V

    throw v0
    :try_end_1
    .catch Lantlr/CharStreamException; {:try_start_1 .. :try_end_1} :catch_0

    :goto_2
    instance-of v0, p0, Lantlr/CharStreamIOException;

    if-eqz v0, :cond_3

    new-instance v0, Lantlr/TokenStreamIOException;

    check-cast p0, Lantlr/CharStreamIOException;

    iget-object p0, p0, Lantlr/CharStreamIOException;->io:Ljava/io/IOException;

    invoke-direct {v0, p0}, Lantlr/TokenStreamIOException;-><init>(Ljava/io/IOException;)V

    throw v0

    :cond_3
    new-instance v0, Lantlr/TokenStreamException;

    invoke-virtual {p0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Lantlr/TokenStreamException;-><init>(Ljava/lang/String;)V

    throw v0
.end method

.method public reportError(Lantlr/RecognitionException;)V
    .locals 3

    iget-object v0, p0, Lantlr/actions/cpp/ActionLexer;->antlrTool:Lantlr/Tool;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "Syntax error in action: "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-virtual {v0, p1, v1, v2, p0}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void
.end method

.method public reportError(Ljava/lang/String;)V
    .locals 3

    iget-object v0, p0, Lantlr/actions/cpp/ActionLexer;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-virtual {v0, p1, v1, v2, p0}, Lantlr/Tool;->error(Ljava/lang/String;Ljava/lang/String;II)V

    return-void
.end method

.method public reportWarning(Ljava/lang/String;)V
    .locals 3

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v0

    if-nez v0, :cond_0

    iget-object p0, p0, Lantlr/actions/cpp/ActionLexer;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0, p1}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    iget-object v0, p0, Lantlr/actions/cpp/ActionLexer;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-virtual {v0, p1, v1, v2, p0}, Lantlr/Tool;->warning(Ljava/lang/String;Ljava/lang/String;II)V

    :goto_0
    return-void
.end method

.method public setLineOffset(I)V
    .locals 0

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->setLine(I)V

    return-void
.end method

.method public setTool(Lantlr/Tool;)V
    .locals 0

    iput-object p1, p0, Lantlr/actions/cpp/ActionLexer;->antlrTool:Lantlr/Tool;

    return-void
.end method
