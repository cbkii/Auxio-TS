.class public Lantlr/preprocessor/PreprocessorLexer;
.super Lantlr/CharScanner;
.source ""

# interfaces
.implements Lantlr/preprocessor/PreprocessorTokenTypes;
.implements Lantlr/TokenStream;


# static fields
.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_1:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_10:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_2:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_3:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_4:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_5:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_6:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_7:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_8:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_9:Lantlr/collections/impl/BitSet;


# direct methods
.method public static constructor <clinit>()V
    .locals 2

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_1()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_2()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_3()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_4()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_5()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_6()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_7()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_7:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_8()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_8:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_9()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_9:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/preprocessor/PreprocessorLexer;->mk_tokenSet_10()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_10:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 1

    new-instance v0, Lantlr/LexerSharedInputState;

    invoke-direct {v0, p1}, Lantlr/LexerSharedInputState;-><init>(Lantlr/InputBuffer;)V

    invoke-direct {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;-><init>(Lantlr/LexerSharedInputState;)V

    return-void
.end method

.method public constructor <init>(Lantlr/LexerSharedInputState;)V
    .locals 2

    invoke-direct {p0, p1}, Lantlr/CharScanner;-><init>(Lantlr/LexerSharedInputState;)V

    const/4 p1, 0x1

    iput-boolean p1, p0, Lantlr/CharScanner;->caseSensitiveLiterals:Z

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->setCaseSensitive(Z)V

    new-instance p1, Ljava/util/Hashtable;

    invoke-direct {p1}, Ljava/util/Hashtable;-><init>()V

    iput-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "public"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x12

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "class"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x8

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "throws"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x17

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "catch"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x1a

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "private"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x11

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "extends"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0xa

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "protected"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x10

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "returns"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x15

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "tokens"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/4 v1, 0x4

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "exception"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 p0, 0x19

    invoke-static {p0, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    return-void
.end method

.method public constructor <init>(Ljava/io/InputStream;)V
    .locals 1

    new-instance v0, Lantlr/ByteBuffer;

    invoke-direct {v0, p1}, Lantlr/ByteBuffer;-><init>(Ljava/io/InputStream;)V

    invoke-direct {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public constructor <init>(Ljava/io/Reader;)V
    .locals 1

    new-instance v0, Lantlr/CharBuffer;

    invoke-direct {v0, p1}, Lantlr/CharBuffer;-><init>(Ljava/io/Reader;)V

    invoke-direct {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public static final mk_tokenSet_0()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x800000000000008L

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

.method public static final mk_tokenSet_10()[J
    .locals 1

    const/4 v0, 0x5

    new-array v0, v0, [J

    fill-array-data v0, :array_0

    return-object v0

    nop

    :array_0
    .array-data 8
        0x800100002600L
        0x800000000000000L
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

    const-wide v2, -0x20000000008L

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

.method public static final mk_tokenSet_3()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x800020000000008L

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
        0x1000000000000000L
        0x0
        0x0
        0x0
    .end array-data
.end method

.method public static final mk_tokenSet_5()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x800838400002408L

    aput-wide v2, v0, v1

    const/4 v1, 0x1

    const-wide v2, -0x800000000000001L

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

.method public static final mk_tokenSet_6()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x8000000008L

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
        0x800500002600L
        0x800000000000000L
        0x0
        0x0
        0x0
    .end array-data
.end method


# virtual methods
.method public final mACTION(Z)V
    .locals 11

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x7b

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :goto_0
    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x7d

    if-ne v3, v4, :cond_0

    goto/16 :goto_1

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0xa

    const/4 v6, 0x0

    const/16 v7, 0xff

    const/4 v8, 0x3

    const/4 v9, 0x2

    if-eq v3, v5, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0xd

    if-ne v3, v5, :cond_2

    :cond_1
    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_2

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v7, :cond_2

    invoke-virtual {p0, v6}, Lantlr/preprocessor/PreprocessorLexer;->mNEWLINE(Z)V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v1, :cond_3

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_3

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v7, :cond_3

    invoke-virtual {p0, v6}, Lantlr/preprocessor/PreprocessorLexer;->mACTION(Z)V

    goto :goto_0

    :cond_3
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0x27

    if-ne v3, v5, :cond_4

    sget-object v3, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v3, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_4

    invoke-virtual {p0, v6}, Lantlr/preprocessor/PreprocessorLexer;->mCHAR_LITERAL(Z)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0x2f

    if-ne v3, v5, :cond_6

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v10, 0x2a

    if-eq v3, v10, :cond_5

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v5, :cond_6

    :cond_5
    invoke-virtual {p0, v6}, Lantlr/preprocessor/PreprocessorLexer;->mCOMMENT(Z)V

    goto :goto_0

    :cond_6
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0x22

    if-ne v3, v5, :cond_7

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_7

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v7, :cond_7

    invoke-virtual {p0, v6}, Lantlr/preprocessor/PreprocessorLexer;->mSTRING_LITERAL(Z)V

    goto/16 :goto_0

    :cond_7
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_8

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v7, :cond_8

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v8, :cond_8

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v7, :cond_8

    const v2, 0xffff

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->matchNot(C)V

    goto/16 :goto_0

    :cond_8
    :goto_1
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_9

    const/4 p1, 0x7

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

    :cond_9
    const/4 p1, 0x0

    :goto_2
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mALT(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    :goto_0
    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual {v1, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    const/4 v1, 0x2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x3

    if-lt v2, v3, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0xff

    if-gt v1, v2, :cond_0

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/preprocessor/PreprocessorLexer;->mELEMENT(Z)V

    goto :goto_0

    :cond_0
    if-eqz p1, :cond_1

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

    goto :goto_1

    :cond_1
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mARG_ACTION(Z)V
    .locals 10

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x5b

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :goto_0
    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x5d

    if-ne v3, v4, :cond_0

    goto/16 :goto_1

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/4 v5, 0x0

    const/16 v6, 0xff

    const/4 v7, 0x3

    const/4 v8, 0x2

    if-ne v3, v1, :cond_1

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v7, :cond_1

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_1

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mARG_ACTION(Z)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v9, 0xa

    if-eq v3, v9, :cond_2

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v9, 0xd

    if-ne v3, v9, :cond_3

    :cond_2
    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v7, :cond_3

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_3

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mNEWLINE(Z)V

    goto :goto_0

    :cond_3
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v9, 0x27

    if-ne v3, v9, :cond_4

    sget-object v3, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v9

    invoke-virtual {v3, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_4

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mCHAR_LITERAL(Z)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v9, 0x22

    if-ne v3, v9, :cond_5

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v7, :cond_5

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_5

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mSTRING_LITERAL(Z)V

    goto :goto_0

    :cond_5
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v7, :cond_6

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_6

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_6

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_6

    const v2, 0xffff

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->matchNot(C)V

    goto/16 :goto_0

    :cond_6
    :goto_1
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_7

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

    goto :goto_2

    :cond_7
    const/4 p1, 0x0

    :goto_2
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mASSIGN_RHS(Z)V
    .locals 9

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    const/16 v2, 0x3d

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x3b

    if-ne v2, v3, :cond_0

    goto/16 :goto_1

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x22

    const/4 v5, 0x0

    const/16 v6, 0xff

    const/4 v7, 0x3

    const/4 v8, 0x2

    if-ne v2, v4, :cond_1

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_1

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_1

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mSTRING_LITERAL(Z)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x27

    if-ne v2, v4, :cond_2

    sget-object v2, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v2, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_2

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mCHAR_LITERAL(Z)V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0xa

    if-eq v2, v4, :cond_3

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0xd

    if-ne v2, v4, :cond_4

    :cond_3
    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_4

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_4

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mNEWLINE(Z)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_5

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_5

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v7, :cond_5

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_5

    const v1, 0xffff

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    goto :goto_0

    :cond_5
    :goto_1
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_6

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

    goto :goto_2

    :cond_6
    const/4 p1, 0x0

    :goto_2
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mBANG(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x21

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mCHAR_LITERAL(Z)V
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

    invoke-virtual {p0, v2}, Lantlr/preprocessor/PreprocessorLexer;->mESC(Z)V

    goto :goto_0

    :cond_0
    sget-object v3, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_8:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_1

    const/16 p1, 0x26

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

.method public final mCOMMA(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x2c

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mCOMMENT(Z)V
    .locals 4

    iget-object p1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p1}, Lantlr/ANTLRStringBuffer;->length()I

    const/4 p1, 0x1

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    const/4 v1, 0x0

    const/4 v2, 0x2

    const/16 v3, 0x2f

    if-ne v0, v3, :cond_0

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    if-ne v0, v3, :cond_0

    invoke-virtual {p0, v1}, Lantlr/preprocessor/PreprocessorLexer;->mSL_COMMENT(Z)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    if-ne v0, v3, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    const/16 v2, 0x2a

    if-ne v0, v2, :cond_1

    invoke-virtual {p0, v1}, Lantlr/preprocessor/PreprocessorLexer;->mML_COMMENT(Z)V

    :goto_0
    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_1
    new-instance v0, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result p1

    invoke-virtual {p0}, Lantlr/CharScanner;->getFilename()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v2

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result p0

    invoke-direct {v0, p1, v1, v2, p0}, Lantlr/NoViableAltForCharException;-><init>(CLjava/lang/String;II)V

    throw v0
.end method

.method public final mCURLY_BLOCK_SCARF(Z)V
    .locals 10

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x7b

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x7d

    if-ne v2, v3, :cond_0

    goto/16 :goto_1

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0xa

    const/4 v5, 0x0

    const/16 v6, 0xff

    const/4 v7, 0x3

    const/4 v8, 0x2

    if-eq v2, v4, :cond_1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0xd

    if-ne v2, v4, :cond_2

    :cond_1
    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_2

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_2

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mNEWLINE(Z)V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x22

    if-ne v2, v4, :cond_3

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_3

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v6, :cond_3

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mSTRING_LITERAL(Z)V

    goto :goto_0

    :cond_3
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x27

    if-ne v2, v4, :cond_4

    sget-object v2, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_6:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v2, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_4

    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mCHAR_LITERAL(Z)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x2f

    if-ne v2, v4, :cond_6

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v9, 0x2a

    if-eq v2, v9, :cond_5

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_6

    :cond_5
    invoke-virtual {p0, v5}, Lantlr/preprocessor/PreprocessorLexer;->mCOMMENT(Z)V

    goto :goto_0

    :cond_6
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v7, :cond_7

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_7

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v7, :cond_7

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v6, :cond_7

    const v1, 0xffff

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    goto/16 :goto_0

    :cond_7
    :goto_1
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_8

    const/16 p1, 0x20

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

    const/16 p1, 0x29

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

.method public final mELEMENT(Z)V
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x0

    const/16 v4, 0xa

    if-eq v2, v4, :cond_6

    const/16 v4, 0xd

    if-eq v2, v4, :cond_6

    const/16 v4, 0x22

    if-eq v2, v4, :cond_5

    const/16 v4, 0x2f

    if-eq v2, v4, :cond_4

    const/16 v4, 0x7b

    if-eq v2, v4, :cond_3

    const/16 v4, 0x27

    if-eq v2, v4, :cond_2

    const/16 v4, 0x28

    if-eq v2, v4, :cond_1

    sget-object v2, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_0

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Lantlr/collections/impl/BitSet;)V

    goto :goto_0

    :cond_0
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
    invoke-virtual {p0, v3}, Lantlr/preprocessor/PreprocessorLexer;->mSUBRULE_BLOCK(Z)V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v3}, Lantlr/preprocessor/PreprocessorLexer;->mCHAR_LITERAL(Z)V

    goto :goto_0

    :cond_3
    invoke-virtual {p0, v3}, Lantlr/preprocessor/PreprocessorLexer;->mACTION(Z)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v3}, Lantlr/preprocessor/PreprocessorLexer;->mCOMMENT(Z)V

    goto :goto_0

    :cond_5
    invoke-virtual {p0, v3}, Lantlr/preprocessor/PreprocessorLexer;->mSTRING_LITERAL(Z)V

    goto :goto_0

    :cond_6
    invoke-virtual {p0, v3}, Lantlr/preprocessor/PreprocessorLexer;->mNEWLINE(Z)V

    :goto_0
    if-eqz p1, :cond_7

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

    goto :goto_1

    :cond_7
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mESC(Z)V
    .locals 10

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x5c

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x22

    if-eq v3, v4, :cond_a

    const/16 v4, 0x27

    if-eq v3, v4, :cond_a

    if-eq v3, v1, :cond_9

    const/16 v1, 0x66

    if-eq v3, v1, :cond_9

    const/16 v1, 0x6e

    if-eq v3, v1, :cond_9

    const/16 v1, 0x72

    if-eq v3, v1, :cond_9

    const/16 v1, 0x77

    if-eq v3, v1, :cond_9

    const/16 v1, 0x61

    if-eq v3, v1, :cond_9

    const/16 v1, 0x62

    if-eq v3, v1, :cond_8

    const/16 v1, 0x74

    if-eq v3, v1, :cond_7

    const/16 v1, 0x75

    const/4 v4, 0x0

    if-eq v3, v1, :cond_6

    const/16 v1, 0x39

    const/16 v5, 0x30

    const/4 v6, 0x2

    const/16 v7, 0xff

    const/4 v8, 0x3

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

    const/16 v9, 0x37

    invoke-virtual {p0, v3, v9}, Lantlr/CharScanner;->matchRange(CC)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v5, :cond_0

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v1, :cond_0

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v8, :cond_0

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v7, :cond_0

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v8, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v7, :cond_1

    goto/16 :goto_2

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

    invoke-virtual {p0, v5, v3}, Lantlr/CharScanner;->matchRange(CC)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v5, :cond_4

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v1, :cond_4

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_4

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v7, :cond_4

    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mDIGIT(Z)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v5, :cond_2

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v1, :cond_2

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v8, :cond_2

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v7, :cond_2

    :goto_0
    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mDIGIT(Z)V

    goto :goto_2

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v8, :cond_3

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v7, :cond_3

    goto :goto_2

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

    if-lt v1, v8, :cond_5

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v7, :cond_5

    goto :goto_2

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
    const/16 v1, 0x75

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mXDIGIT(Z)V

    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mXDIGIT(Z)V

    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mXDIGIT(Z)V

    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mXDIGIT(Z)V

    goto :goto_2

    :cond_7
    const/16 v1, 0x74

    goto :goto_1

    :cond_8
    const/16 v1, 0x62

    :cond_9
    :goto_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_a
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_2
    if-eqz p1, :cond_b

    const/16 p1, 0x28

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

    :cond_b
    const/4 p1, 0x0

    :goto_3
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
    .locals 6

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x5f

    packed-switch v2, :pswitch_data_0

    :pswitch_0
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
    const/16 v2, 0x41

    const/16 v4, 0x5a

    :goto_0
    invoke-virtual {p0, v2, v4}, Lantlr/CharScanner;->matchRange(CC)V

    :goto_1
    const/4 v2, 0x0

    const/16 v4, 0x9

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    if-eq v5, v3, :cond_1

    packed-switch v5, :pswitch_data_1

    packed-switch v5, :pswitch_data_2

    packed-switch v5, :pswitch_data_3

    new-instance v1, Ljava/lang/String;

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v3

    iget-object v5, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v5

    sub-int/2addr v5, v0

    invoke-direct {v1, v3, v0, v5}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p0, v1, v4}, Lantlr/CharScanner;->testLiteralsTable(Ljava/lang/String;I)I

    move-result v1

    if-eqz p1, :cond_0

    const/4 p1, -0x1

    if-eq v1, p1, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

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

    goto :goto_2

    :pswitch_2
    const/16 v2, 0x30

    const/16 v4, 0x39

    goto :goto_0

    :pswitch_3
    const/16 v2, 0x61

    const/16 v4, 0x7a

    goto :goto_0

    :cond_0
    :goto_2
    iput-object v2, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_1
    :pswitch_4
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    goto :goto_1

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
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_4
        :pswitch_0
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

    :pswitch_data_1
    .packed-switch 0x30
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
    .end packed-switch

    :pswitch_data_3
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

.method public final mID_OR_KEYWORD(Z)V
    .locals 16

    move-object/from16 v0, p0

    iget-object v1, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    const/4 v2, 0x1

    invoke-virtual {v0, v2}, Lantlr/preprocessor/PreprocessorLexer;->mID(Z)V

    iget-object v3, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v3}, Lantlr/Token;->getType()I

    move-result v4

    sget-object v5, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_9:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    invoke-virtual {v5, v6}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v5

    const/16 v7, 0xff

    const/4 v8, 0x3

    const/16 v9, 0x2f

    const/16 v10, 0x20

    const/16 v11, 0xa

    const/16 v12, 0x9

    const/16 v13, 0xd

    const/4 v14, 0x2

    const/4 v15, 0x0

    if-eqz v5, :cond_7

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    if-lt v5, v8, :cond_7

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    if-gt v5, v7, :cond_7

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v5

    const-string v6, "header"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_7

    sget-object v3, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_0

    sget-object v3, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_9:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    goto :goto_0

    :cond_0
    sget-object v3, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_9:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_6

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_6

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v7, :cond_6

    :goto_0
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v12, :cond_3

    if-eq v3, v11, :cond_3

    if-eq v3, v13, :cond_3

    if-eq v3, v10, :cond_3

    const/16 v4, 0x22

    if-eq v3, v4, :cond_2

    if-eq v3, v9, :cond_3

    const/16 v4, 0x7b

    if-ne v3, v4, :cond_1

    goto :goto_1

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
    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mSTRING_LITERAL(Z)V

    :cond_3
    :goto_1
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v12, :cond_5

    if-eq v3, v11, :cond_5

    if-eq v3, v13, :cond_5

    if-eq v3, v10, :cond_5

    if-eq v3, v9, :cond_4

    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mACTION(Z)V

    const/4 v4, 0x5

    goto/16 :goto_4

    :cond_4
    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mCOMMENT(Z)V

    goto :goto_1

    :cond_5
    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    goto :goto_1

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
    sget-object v5, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_10:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    invoke-virtual {v5, v6}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v5

    if-eqz v5, :cond_a

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    if-lt v5, v8, :cond_a

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    if-gt v5, v7, :cond_a

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v5

    const-string v6, "tokens"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_a

    :goto_2
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v12, :cond_9

    if-eq v3, v11, :cond_9

    if-eq v3, v13, :cond_9

    if-eq v3, v10, :cond_9

    if-eq v3, v9, :cond_8

    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mCURLY_BLOCK_SCARF(Z)V

    const/16 v4, 0xc

    goto :goto_4

    :cond_8
    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mCOMMENT(Z)V

    goto :goto_2

    :cond_9
    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    goto :goto_2

    :cond_a
    sget-object v5, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_10:Lantlr/collections/impl/BitSet;

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    invoke-virtual {v5, v6}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v5

    if-eqz v5, :cond_d

    invoke-virtual {v3}, Lantlr/Token;->getText()Ljava/lang/String;

    move-result-object v3

    const-string v5, "options"

    invoke-virtual {v3, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_d

    :goto_3
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v12, :cond_c

    if-eq v3, v11, :cond_c

    if-eq v3, v13, :cond_c

    if-eq v3, v10, :cond_c

    if-eq v3, v9, :cond_b

    const/16 v3, 0x7b

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(C)V

    move v4, v13

    goto :goto_4

    :cond_b
    const/16 v3, 0x7b

    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mCOMMENT(Z)V

    goto :goto_3

    :cond_c
    const/16 v3, 0x7b

    invoke-virtual {v0, v15}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    goto :goto_3

    :cond_d
    :goto_4
    if-eqz p1, :cond_e

    const/4 v2, -0x1

    if-eq v4, v2, :cond_e

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

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

    goto :goto_5

    :cond_e
    const/4 v2, 0x0

    :goto_5
    iput-object v2, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mLPAREN(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x28

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    const/16 p1, 0x1d

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

.method public final mML_COMMENT(Z)V
    .locals 7

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

    goto :goto_1

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xa

    const/16 v5, 0xff

    const/4 v6, 0x3

    if-eq v2, v3, :cond_1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xd

    if-ne v2, v3, :cond_2

    :cond_1
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v5, :cond_2

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/preprocessor/PreprocessorLexer;->mNEWLINE(Z)V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_3

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v5, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v6, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v5, :cond_3

    const v1, 0xffff

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    goto :goto_0

    :cond_3
    :goto_1
    const-string v1, "*/"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    if-eqz p1, :cond_4

    const/16 p1, 0x25

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

    :cond_4
    const/4 p1, 0x0

    :goto_2
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mNEWLINE(Z)V
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xa

    const/16 v4, 0xd

    if-ne v2, v4, :cond_0

    const/4 v2, 0x2

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v3, :cond_0

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_0
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    goto :goto_1

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_1

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_1
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto :goto_2

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v3, :cond_3

    goto :goto_0

    :goto_2
    if-eqz p1, :cond_2

    const/16 p1, 0x22

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

    :cond_2
    const/4 p1, 0x0

    :goto_3
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

.method public final mRCURLY(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x7d

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mRPAREN(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x29

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    const/16 p1, 0x1e

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

.method public final mRULE_BLOCK(Z)V
    .locals 13

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3a

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    const/4 v3, 0x2

    const/4 v4, 0x0

    if-eqz v1, :cond_0

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v1, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    iget-object v5, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_0

    :cond_0
    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v1, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_9

    :goto_0
    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mALT(Z)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v5, 0x20

    const/16 v6, 0xd

    const/16 v7, 0xa

    const/16 v8, 0x9

    const/16 v9, 0x3b

    const/16 v10, 0x7c

    if-eq v1, v8, :cond_2

    if-eq v1, v7, :cond_2

    if-eq v1, v6, :cond_2

    if-eq v1, v5, :cond_2

    if-eq v1, v9, :cond_3

    if-ne v1, v10, :cond_1

    goto :goto_1

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

    :cond_2
    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    iget-object v11, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_3
    :goto_1
    const/4 v1, 0x0

    const/16 v11, 0x16

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v12

    if-ne v12, v10, :cond_7

    invoke-virtual {p0, v10}, Lantlr/CharScanner;->match(C)V

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v11

    invoke-virtual {v1, v11}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_4

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v11

    invoke-virtual {v1, v11}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_4

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    iget-object v11, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_2

    :cond_4
    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v11

    invoke-virtual {v1, v11}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_6

    :goto_2
    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mALT(Z)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-eq v1, v8, :cond_2

    if-eq v1, v7, :cond_2

    if-eq v1, v6, :cond_2

    if-eq v1, v5, :cond_2

    if-eq v1, v9, :cond_3

    if-ne v1, v10, :cond_5

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

    :cond_7
    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_8

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v1

    new-instance p1, Ljava/lang/String;

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v2

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    sub-int/2addr v3, v0

    invoke-direct {p1, v2, v0, v3}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {v1, p1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    :cond_8
    iput-object v1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_9
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

.method public final mSEMI(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3b

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mSL_COMMENT(Z)V
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const-string v1, "//"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xa

    if-eq v2, v3, :cond_1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xd

    if-ne v2, v3, :cond_0

    goto :goto_1

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x3

    if-lt v2, v3, :cond_1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0xff

    if-gt v1, v2, :cond_1

    const/4 v1, 0x2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    if-lt v4, v3, :cond_1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v2, :cond_1

    const v1, 0xffff

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    goto :goto_0

    :cond_1
    :goto_1
    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/preprocessor/PreprocessorLexer;->mNEWLINE(Z)V

    if-eqz p1, :cond_2

    const/16 p1, 0x24

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

    :cond_2
    const/4 p1, 0x0

    :goto_2
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mSTRING_LITERAL(Z)V
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

    invoke-virtual {p0, v2}, Lantlr/preprocessor/PreprocessorLexer;->mESC(Z)V

    goto :goto_0

    :cond_0
    sget-object v3, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_7:Lantlr/collections/impl/BitSet;

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

    const/16 p1, 0x27

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

.method public final mSUBRULE_BLOCK(Z)V
    .locals 10

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x28

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    const/4 v3, 0x2

    const/4 v4, 0x0

    if-eqz v1, :cond_0

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v1, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    :goto_0
    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    goto :goto_1

    :cond_0
    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v1, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_d

    :goto_1
    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mALT(Z)V

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v1, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    const/16 v5, 0x20

    const/16 v6, 0xd

    const/16 v7, 0xa

    const/16 v8, 0x9

    if-eqz v1, :cond_5

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v9

    invoke-virtual {v1, v9}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_5

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v9, 0x7c

    if-eq v1, v8, :cond_2

    if-eq v1, v7, :cond_2

    if-eq v1, v6, :cond_2

    if-eq v1, v5, :cond_2

    if-ne v1, v9, :cond_1

    goto :goto_2

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

    :cond_2
    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    :goto_2
    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v1, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_3

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v1, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_3

    goto :goto_0

    :cond_3
    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v5

    invoke-virtual {v1, v5}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_4

    goto :goto_1

    :cond_4
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

    :cond_5
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v9, 0x29

    if-eq v1, v8, :cond_7

    if-eq v1, v7, :cond_7

    if-eq v1, v6, :cond_7

    if-eq v1, v5, :cond_7

    if-ne v1, v9, :cond_6

    goto :goto_3

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

    :cond_7
    invoke-virtual {p0, v4}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    :goto_3
    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v4, 0x3d

    if-ne v1, v4, :cond_8

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v3, 0x3e

    if-ne v1, v3, :cond_8

    const-string v1, "=>"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    goto :goto_5

    :cond_8
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v3, 0x2a

    if-ne v1, v3, :cond_9

    :goto_4
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    goto :goto_5

    :cond_9
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v3, 0x2b

    if-ne v1, v3, :cond_a

    goto :goto_4

    :cond_a
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0x3f

    if-ne v1, v2, :cond_b

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    :cond_b
    :goto_5
    if-eqz p1, :cond_c

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

    goto :goto_6

    :cond_c
    const/4 p1, 0x0

    :goto_6
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_d
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

.method public final mWS(Z)V
    .locals 4

    iget-object p1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p1}, Lantlr/ANTLRStringBuffer;->length()I

    const/4 p1, 0x0

    move v0, p1

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x20

    if-ne v2, v3, :cond_0

    :goto_1
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    goto :goto_3

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x9

    if-ne v2, v3, :cond_1

    goto :goto_1

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xa

    if-eq v2, v3, :cond_4

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xd

    if-ne v2, v3, :cond_2

    goto :goto_2

    :cond_2
    if-lt v0, v1, :cond_3

    const/4 p1, 0x0

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

    :cond_4
    :goto_2
    invoke-virtual {p0, p1}, Lantlr/preprocessor/PreprocessorLexer;->mNEWLINE(Z)V

    :goto_3
    add-int/lit8 v0, v0, 0x1

    goto :goto_0
.end method

.method public final mXDIGIT(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

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
    const/16 v1, 0x30

    const/16 v2, 0x39

    :goto_0
    invoke-virtual {p0, v1, v2}, Lantlr/CharScanner;->matchRange(CC)V

    goto :goto_1

    :pswitch_1
    const/16 v1, 0x41

    const/16 v2, 0x46

    goto :goto_0

    :pswitch_2
    const/16 v1, 0x61

    const/16 v2, 0x66

    goto :goto_0

    :goto_1
    if-eqz p1, :cond_0

    const/16 p1, 0x2a

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

    :cond_0
    const/4 p1, 0x0

    :goto_2
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
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x41
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x61
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
    .end packed-switch
.end method

.method public nextToken()Lantlr/Token;
    .locals 4

    :goto_0
    invoke-virtual {p0}, Lantlr/CharScanner;->resetText()V

    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0x9

    if-eq v1, v2, :cond_c

    const/16 v2, 0xa

    if-eq v1, v2, :cond_c

    const/16 v2, 0xd

    if-eq v1, v2, :cond_c

    const/16 v2, 0x27

    if-eq v1, v2, :cond_b

    const/16 v2, 0x29

    if-eq v1, v2, :cond_a

    const/16 v2, 0x2c

    if-eq v1, v2, :cond_9

    const/16 v2, 0x2f

    if-eq v1, v2, :cond_8

    const/16 v2, 0x3d

    if-eq v1, v2, :cond_7

    const/16 v2, 0x5f

    if-eq v1, v2, :cond_6

    const/16 v2, 0x7d

    if-eq v1, v2, :cond_5

    const/16 v2, 0x3a

    if-eq v1, v2, :cond_4

    const/16 v2, 0x3b

    if-eq v1, v2, :cond_3

    packed-switch v1, :pswitch_data_0

    packed-switch v1, :pswitch_data_1

    packed-switch v1, :pswitch_data_2

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    goto :goto_1

    :pswitch_0
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mSTRING_LITERAL(Z)V

    goto/16 :goto_2

    :pswitch_1
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mBANG(Z)V

    goto/16 :goto_2

    :pswitch_2
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mARG_ACTION(Z)V

    goto/16 :goto_2

    :pswitch_3
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mACTION(Z)V

    goto/16 :goto_2

    :goto_1
    const/16 v2, 0x28

    if-ne v1, v2, :cond_0

    sget-object v1, Lantlr/preprocessor/PreprocessorLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    const/4 v3, 0x2

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mSUBRULE_BLOCK(Z)V

    goto :goto_2

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v2, :cond_1

    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mLPAREN(Z)V

    goto :goto_2

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const v2, 0xffff

    if-ne v1, v2, :cond_2

    invoke-virtual {p0}, Lantlr/CharScanner;->uponEOF()V

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v0

    iput-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    goto :goto_2

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

    :cond_3
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mSEMI(Z)V

    goto :goto_2

    :cond_4
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mRULE_BLOCK(Z)V

    goto :goto_2

    :cond_5
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mRCURLY(Z)V

    goto :goto_2

    :cond_6
    :pswitch_4
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mID_OR_KEYWORD(Z)V

    goto :goto_2

    :cond_7
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mASSIGN_RHS(Z)V

    goto :goto_2

    :cond_8
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mCOMMENT(Z)V

    goto :goto_2

    :cond_9
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mCOMMA(Z)V

    goto :goto_2

    :cond_a
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mRPAREN(Z)V

    goto :goto_2

    :cond_b
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mCHAR_LITERAL(Z)V

    goto :goto_2

    :cond_c
    :pswitch_5
    invoke-virtual {p0, v0}, Lantlr/preprocessor/PreprocessorLexer;->mWS(Z)V

    :goto_2
    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    if-nez v0, :cond_d

    goto/16 :goto_0

    :cond_d
    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getType()I

    move-result v0

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->testLiteralsTable(I)I

    move-result v0

    iget-object v1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v1, v0}, Lantlr/Token;->setType(I)V

    iget-object p0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Lantlr/CharStreamException; {:try_start_0 .. :try_end_0} :catch_0

    return-object p0

    :catch_0
    move-exception p0

    goto :goto_3

    :catch_1
    move-exception p0

    :try_start_1
    new-instance v0, Lantlr/TokenStreamRecognitionException;

    invoke-direct {v0, p0}, Lantlr/TokenStreamRecognitionException;-><init>(Lantlr/RecognitionException;)V

    throw v0
    :try_end_1
    .catch Lantlr/CharStreamException; {:try_start_1 .. :try_end_1} :catch_0

    :goto_3
    instance-of v0, p0, Lantlr/CharStreamIOException;

    if-eqz v0, :cond_e

    new-instance v0, Lantlr/TokenStreamIOException;

    check-cast p0, Lantlr/CharStreamIOException;

    iget-object p0, p0, Lantlr/CharStreamIOException;->io:Ljava/io/IOException;

    invoke-direct {v0, p0}, Lantlr/TokenStreamIOException;-><init>(Ljava/io/IOException;)V

    throw v0

    :cond_e
    new-instance v0, Lantlr/TokenStreamException;

    invoke-virtual {p0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Lantlr/TokenStreamException;-><init>(Ljava/lang/String;)V

    throw v0

    :pswitch_data_0
    .packed-switch 0x20
        :pswitch_5
        :pswitch_1
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x41
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_2
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x61
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_3
    .end packed-switch
.end method
