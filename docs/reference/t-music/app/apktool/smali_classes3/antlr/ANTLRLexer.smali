.class public Lantlr/ANTLRLexer;
.super Lantlr/CharScanner;
.source ""

# interfaces
.implements Lantlr/ANTLRTokenTypes;
.implements Lantlr/TokenStream;


# static fields
.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_1:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_2:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_3:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_4:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_5:Lantlr/collections/impl/BitSet;


# direct methods
.method public static constructor <clinit>()V
    .locals 2

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRLexer;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRLexer;->mk_tokenSet_1()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRLexer;->mk_tokenSet_2()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRLexer;->mk_tokenSet_3()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRLexer;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRLexer;->mk_tokenSet_4()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRLexer;->mk_tokenSet_5()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRLexer;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 1

    new-instance v0, Lantlr/LexerSharedInputState;

    invoke-direct {v0, p1}, Lantlr/LexerSharedInputState;-><init>(Lantlr/InputBuffer;)V

    invoke-direct {p0, v0}, Lantlr/ANTLRLexer;-><init>(Lantlr/LexerSharedInputState;)V

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

    const/16 v1, 0x1f

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "class"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0xa

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "header"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/4 v1, 0x5

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "throws"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x25

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "lexclass"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x9

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "catch"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x28

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "private"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x20

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "options"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x33

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "extends"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0xb

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "protected"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x1e

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "TreeParser"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0xd

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "Parser"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x1d

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "Lexer"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0xc

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "returns"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x23

    invoke-static {v1, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    iget-object p1, p0, Lantlr/CharScanner;->literals:Ljava/util/Hashtable;

    new-instance v0, Lantlr/ANTLRHashString;

    const-string v1, "charVocabulary"

    invoke-direct {v0, v1, p0}, Lantlr/ANTLRHashString;-><init>(Ljava/lang/String;Lantlr/CharScanner;)V

    const/16 v1, 0x12

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

    const/16 p0, 0x27

    invoke-static {p0, p1, v0}, La/a/a/a/a;->a(ILjava/util/Hashtable;Lantlr/ANTLRHashString;)V

    return-void
.end method

.method public constructor <init>(Ljava/io/InputStream;)V
    .locals 1

    new-instance v0, Lantlr/ByteBuffer;

    invoke-direct {v0, p1}, Lantlr/ByteBuffer;-><init>(Ljava/io/InputStream;)V

    invoke-direct {p0, v0}, Lantlr/ANTLRLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public constructor <init>(Ljava/io/Reader;)V
    .locals 1

    new-instance v0, Lantlr/CharBuffer;

    invoke-direct {v0, p1}, Lantlr/CharBuffer;-><init>(Ljava/io/Reader;)V

    invoke-direct {p0, v0}, Lantlr/ANTLRLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public static escapeCharValue(Ljava/lang/String;)I
    .locals 8

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result v0

    const/4 v1, 0x0

    const/16 v2, 0x5c

    if-eq v0, v2, :cond_0

    return v1

    :cond_0
    const/4 v0, 0x2

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result v3

    const/16 v4, 0x22

    if-eq v3, v4, :cond_c

    const/16 v4, 0x27

    if-eq v3, v4, :cond_c

    if-eq v3, v2, :cond_b

    const/16 v2, 0x62

    const/16 v4, 0x8

    if-eq v3, v2, :cond_a

    const/16 v2, 0x66

    if-eq v3, v2, :cond_9

    const/16 v2, 0x6e

    if-eq v3, v2, :cond_8

    const/16 v2, 0x72

    if-eq v3, v2, :cond_7

    const/16 v2, 0x74

    if-eq v3, v2, :cond_6

    const/16 v2, 0x75

    const/4 v5, 0x5

    const/4 v6, 0x4

    const/4 v7, 0x3

    if-eq v3, v2, :cond_4

    packed-switch v3, :pswitch_data_0

    return v1

    :pswitch_0
    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v1

    if-le v1, v6, :cond_1

    invoke-virtual {p0, v7}, Ljava/lang/String;->charAt(I)C

    move-result v1

    invoke-static {v1}, Ljava/lang/Character;->isDigit(C)Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result v0

    add-int/lit8 v0, v0, -0x30

    mul-int/2addr v0, v4

    invoke-virtual {p0, v7}, Ljava/lang/String;->charAt(I)C

    move-result p0

    add-int/lit8 p0, p0, -0x30

    add-int/2addr p0, v0

    return p0

    :cond_1
    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result p0

    add-int/lit8 p0, p0, -0x30

    return p0

    :pswitch_1
    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v1

    if-le v1, v5, :cond_2

    invoke-virtual {p0, v6}, Ljava/lang/String;->charAt(I)C

    move-result v1

    invoke-static {v1}, Ljava/lang/Character;->isDigit(C)Z

    move-result v1

    if-eqz v1, :cond_2

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result v0

    add-int/lit8 v0, v0, -0x30

    mul-int/2addr v0, v4

    mul-int/2addr v0, v4

    invoke-virtual {p0, v7}, Ljava/lang/String;->charAt(I)C

    move-result v1

    add-int/lit8 v1, v1, -0x30

    mul-int/2addr v1, v4

    add-int/2addr v1, v0

    invoke-virtual {p0, v6}, Ljava/lang/String;->charAt(I)C

    move-result p0

    add-int/lit8 p0, p0, -0x30

    add-int/2addr p0, v1

    return p0

    :cond_2
    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v1

    if-le v1, v6, :cond_3

    invoke-virtual {p0, v7}, Ljava/lang/String;->charAt(I)C

    move-result v1

    invoke-static {v1}, Ljava/lang/Character;->isDigit(C)Z

    move-result v1

    if-eqz v1, :cond_3

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result v0

    add-int/lit8 v0, v0, -0x30

    mul-int/2addr v0, v4

    invoke-virtual {p0, v7}, Ljava/lang/String;->charAt(I)C

    move-result p0

    add-int/lit8 p0, p0, -0x30

    add-int/2addr p0, v0

    return p0

    :cond_3
    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result p0

    add-int/lit8 p0, p0, -0x30

    return p0

    :cond_4
    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v0

    if-eq v0, v4, :cond_5

    return v1

    :cond_5
    invoke-virtual {p0, v7}, Ljava/lang/String;->charAt(I)C

    move-result v0

    const/16 v1, 0x10

    invoke-static {v0, v1}, Ljava/lang/Character;->digit(CI)I

    move-result v0

    mul-int/2addr v0, v1

    mul-int/2addr v0, v1

    mul-int/2addr v0, v1

    invoke-virtual {p0, v6}, Ljava/lang/String;->charAt(I)C

    move-result v2

    invoke-static {v2, v1}, Ljava/lang/Character;->digit(CI)I

    move-result v2

    mul-int/2addr v2, v1

    mul-int/2addr v2, v1

    add-int/2addr v2, v0

    invoke-virtual {p0, v5}, Ljava/lang/String;->charAt(I)C

    move-result v0

    invoke-static {v0, v1}, Ljava/lang/Character;->digit(CI)I

    move-result v0

    mul-int/2addr v0, v1

    add-int/2addr v0, v2

    const/4 v2, 0x6

    invoke-virtual {p0, v2}, Ljava/lang/String;->charAt(I)C

    move-result p0

    invoke-static {p0, v1}, Ljava/lang/Character;->digit(CI)I

    move-result p0

    add-int/2addr p0, v0

    return p0

    :cond_6
    const/16 p0, 0x9

    return p0

    :cond_7
    const/16 p0, 0xd

    return p0

    :cond_8
    const/16 p0, 0xa

    return p0

    :cond_9
    const/16 p0, 0xc

    return p0

    :cond_a
    return v4

    :cond_b
    return v2

    :cond_c
    return v4

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

.method public static final mk_tokenSet_0()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide/16 v2, -0x2408

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

.method public static final mk_tokenSet_2()[J
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

.method public static final mk_tokenSet_3()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x8400002408L

    aput-wide v2, v0, v1

    const/4 v1, 0x1

    const-wide/32 v2, -0x28000001

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

.method public static final mk_tokenSet_4()[J
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

.method public static final mk_tokenSet_5()[J
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

.method public static tokenTypeForCharLiteral(Ljava/lang/String;)I
    .locals 2

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v0

    const/4 v1, 0x3

    if-le v0, v1, :cond_0

    invoke-static {p0}, Lantlr/ANTLRLexer;->escapeCharValue(Ljava/lang/String;)I

    move-result p0

    return p0

    :cond_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Ljava/lang/String;->charAt(I)C

    move-result p0

    return p0
.end method


# virtual methods
.method public final mACTION(Z)V
    .locals 7

    iget-object p1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result p1

    invoke-virtual {p0}, Lantlr/CharScanner;->getLine()I

    move-result v0

    invoke-virtual {p0}, Lantlr/CharScanner;->getColumn()I

    move-result v1

    const/4 v2, 0x0

    invoke-virtual {p0, v2}, Lantlr/ANTLRLexer;->mNESTED_ACTION(Z)V

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x3f

    const/4 v4, 0x7

    if-ne v2, v3, :cond_0

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    const/16 v2, 0x2b

    goto :goto_0

    :cond_0
    move v2, v4

    :goto_0
    const-string v3, "{"

    if-ne v2, v4, :cond_1

    invoke-virtual {p0}, Lantlr/CharScanner;->getText()Ljava/lang/String;

    move-result-object v4

    const-string v5, "}"

    goto :goto_1

    :cond_1
    invoke-virtual {p0}, Lantlr/CharScanner;->getText()Ljava/lang/String;

    move-result-object v4

    const-string v5, "}?"

    :goto_1
    invoke-static {v4, v3, v5}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->setText(Ljava/lang/String;)V

    new-instance v3, Lantlr/CommonToken;

    new-instance v4, Ljava/lang/String;

    iget-object v5, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v5}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v5

    iget-object v6, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v6}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v6

    sub-int/2addr v6, p1

    invoke-direct {v4, v5, p1, v6}, Ljava/lang/String;-><init>([CII)V

    invoke-direct {v3, v2, v4}, Lantlr/CommonToken;-><init>(ILjava/lang/String;)V

    invoke-virtual {v3, v0}, Lantlr/CommonToken;->setLine(I)V

    invoke-virtual {v3, v1}, Lantlr/CommonToken;->setColumn(I)V

    iput-object v3, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mARG_ACTION(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mNESTED_ARG_ACTION(Z)V

    invoke-virtual {p0}, Lantlr/CharScanner;->getText()Ljava/lang/String;

    move-result-object v1

    const-string v2, "["

    const-string v3, "]"

    invoke-static {v1, v2, v3}, Lantlr/StringUtils;->stripFrontBack(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->setText(Ljava/lang/String;)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mASSIGN(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3d

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

.method public final mBANG(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x21

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

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

.method public final mCARET(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x5e

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    const/16 p1, 0x31

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

    invoke-virtual {p0, v2}, Lantlr/ANTLRLexer;->mESC(Z)V

    goto :goto_0

    :cond_0
    sget-object v3, Lantlr/ANTLRLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v3, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

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

.method public final mCLOSE_ELEMENT_OPTION(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3e

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mCOLON(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3a

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mCOMMA(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x2c

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mCOMMENT(Z)V
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x2

    const/16 v4, 0x2f

    if-ne v2, v4, :cond_0

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_0

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mSL_COMMENT(Z)V

    const/16 v1, 0x35

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_3

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x2a

    if-ne v2, v3, :cond_3

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mML_COMMENT(Z)V

    iget-object v1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v1}, Lantlr/Token;->getType()I

    move-result v1

    :goto_0
    const/16 v2, 0x8

    const/4 v3, -0x1

    if-eq v1, v2, :cond_1

    move v1, v3

    :cond_1
    if-eqz p1, :cond_2

    if-eq v1, v3, :cond_2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

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

.method public final mDIGIT(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x39

    const/16 v2, 0x30

    invoke-virtual {p0, v2, v1}, Lantlr/CharScanner;->matchRange(CC)V

    if-eqz p1, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

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
    .locals 8

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

    if-eq v3, v1, :cond_6

    const/4 v1, 0x2

    const/16 v4, 0xff

    const/4 v5, 0x3

    const/16 v6, 0x37

    const/16 v7, 0x30

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

    invoke-virtual {p0, v3, v6}, Lantlr/CharScanner;->matchRange(CC)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v7, :cond_0

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v5, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v4, :cond_0

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v5, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v4, :cond_1

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

    invoke-virtual {p0, v7, v3}, Lantlr/CharScanner;->matchRange(CC)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v7, :cond_4

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_4

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v5, :cond_4

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v4, :cond_4

    invoke-virtual {p0, v7, v6}, Lantlr/CharScanner;->matchRange(CC)V

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v7, :cond_2

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v6, :cond_2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v5, :cond_2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v4, :cond_2

    :goto_0
    invoke-virtual {p0, v7, v6}, Lantlr/CharScanner;->matchRange(CC)V

    goto :goto_2

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v5, :cond_3

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v4, :cond_3

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

    if-lt v1, v5, :cond_5

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v4, :cond_5

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

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mXDIGIT(Z)V

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mXDIGIT(Z)V

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mXDIGIT(Z)V

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mXDIGIT(Z)V

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

    const/16 p1, 0x38

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

.method public final mIMPLIES(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const-string v1, "=>"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    if-eqz p1, :cond_0

    const/16 p1, 0x30

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

.method public final mINT(Z)V
    .locals 6

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x0

    :goto_0
    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x30

    if-lt v3, v4, :cond_0

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0x39

    if-gt v3, v5, :cond_0

    invoke-virtual {p0, v4, v5}, Lantlr/CharScanner;->matchRange(CC)V

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    if-lt v1, v2, :cond_2

    if-eqz p1, :cond_1

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

.method public final mINTERNAL_RULE_REF(Z)I
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x7a

    const/16 v2, 0x61

    :pswitch_0
    invoke-virtual {p0, v2, v1}, Lantlr/CharScanner;->matchRange(CC)V

    :goto_0
    const/4 v3, 0x1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x5f

    if-eq v3, v4, :cond_1

    packed-switch v3, :pswitch_data_0

    packed-switch v3, :pswitch_data_1

    packed-switch v3, :pswitch_data_2

    const/16 v1, 0x29

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->testLiteralsTable(I)I

    move-result v1

    if-eqz p1, :cond_0

    const/16 p1, 0x3e

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object p1

    new-instance v2, Ljava/lang/String;

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v3

    iget-object v4, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v4

    sub-int/2addr v4, v0

    invoke-direct {v2, v3, v0, v4}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {p1, v2}, Lantlr/Token;->setText(Ljava/lang/String;)V

    goto :goto_2

    :pswitch_1
    const/16 v3, 0x30

    const/16 v4, 0x39

    :goto_1
    invoke-virtual {p0, v3, v4}, Lantlr/CharScanner;->matchRange(CC)V

    goto :goto_0

    :pswitch_2
    const/16 v3, 0x41

    const/16 v4, 0x5a

    goto :goto_1

    :cond_0
    const/4 p1, 0x0

    :goto_2
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return v1

    :cond_1
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    :pswitch_data_0
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
    .end packed-switch

    :pswitch_data_1
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

.method public final mLPAREN(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x28

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mML_COMMENT(Z)V
    .locals 11

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const-string v1, "/*"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x2f

    const/16 v4, 0x2a

    const/16 v5, 0xff

    const/4 v6, 0x3

    const/4 v7, 0x2

    if-ne v2, v4, :cond_0

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_0

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v5, :cond_0

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v3, :cond_0

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    const/16 v2, 0x8

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_7

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v5, :cond_7

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v6, :cond_7

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v5, :cond_7

    const/16 v2, 0x37

    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-ne v8, v4, :cond_1

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-ne v8, v3, :cond_1

    goto :goto_3

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    const/16 v9, 0xa

    const/16 v10, 0xd

    if-ne v8, v10, :cond_2

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-ne v8, v9, :cond_2

    invoke-virtual {p0, v10}, Lantlr/CharScanner;->match(C)V

    :goto_1
    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    :goto_2
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-ne v8, v10, :cond_3

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-lt v8, v6, :cond_3

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-gt v8, v5, :cond_3

    invoke-virtual {p0, v10}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_3
    sget-object v8, Lantlr/ANTLRLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v10

    invoke-virtual {v8, v10}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-lt v8, v6, :cond_4

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-gt v8, v5, :cond_4

    sget-object v8, Lantlr/ANTLRLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->match(Lantlr/collections/impl/BitSet;)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-ne v8, v9, :cond_5

    goto :goto_1

    :cond_5
    :goto_3
    const-string v1, "*/"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    if-eqz p1, :cond_6

    const/4 p1, -0x1

    if-eq v2, p1, :cond_6

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

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

    :cond_6
    const/4 p1, 0x0

    :goto_4
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

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
.end method

.method public final mNESTED_ACTION(Z)V
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

    goto/16 :goto_3

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v5, 0xd

    const/16 v6, 0xa

    const/16 v7, 0xff

    const/4 v8, 0x3

    const/4 v9, 0x2

    if-eq v3, v6, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v5, :cond_5

    :cond_1
    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_5

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v7, :cond_5

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v5, :cond_2

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v6, :cond_2

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    :goto_1
    invoke-virtual {p0, v6}, Lantlr/CharScanner;->match(C)V

    :goto_2
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v5, :cond_3

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_3

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v7, :cond_3

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_3
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v6, :cond_4

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

    move-result v3

    const/4 v5, 0x0

    if-ne v3, v1, :cond_6

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_6

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v7, :cond_6

    invoke-virtual {p0, v5}, Lantlr/ANTLRLexer;->mNESTED_ACTION(Z)V

    goto/16 :goto_0

    :cond_6
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v6, 0x27

    if-ne v3, v6, :cond_7

    sget-object v3, Lantlr/ANTLRLexer;->_tokenSet_4:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    invoke-virtual {v3, v6}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v3

    if-eqz v3, :cond_7

    invoke-virtual {p0, v5}, Lantlr/ANTLRLexer;->mCHAR_LITERAL(Z)V

    goto/16 :goto_0

    :cond_7
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v6, 0x2f

    if-ne v3, v6, :cond_9

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v10, 0x2a

    if-eq v3, v10, :cond_8

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v6, :cond_9

    :cond_8
    invoke-virtual {p0, v5}, Lantlr/ANTLRLexer;->mCOMMENT(Z)V

    goto/16 :goto_0

    :cond_9
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v6, 0x22

    if-ne v3, v6, :cond_a

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_a

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-gt v3, v7, :cond_a

    invoke-virtual {p0, v5}, Lantlr/ANTLRLexer;->mSTRING_LITERAL(Z)V

    goto/16 :goto_0

    :cond_a
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-lt v3, v8, :cond_b

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v7, :cond_b

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v8, :cond_b

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v7, :cond_b

    const v2, 0xffff

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->matchNot(C)V

    goto/16 :goto_0

    :cond_b
    :goto_3
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_c

    const/16 p1, 0x3c

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

    :cond_c
    const/4 p1, 0x0

    :goto_4
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mNESTED_ARG_ACTION(Z)V
    .locals 7

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x5b

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :goto_0
    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0xa

    if-eq v3, v4, :cond_7

    const/16 v5, 0x22

    const/4 v6, 0x0

    if-eq v3, v5, :cond_6

    const/16 v5, 0x27

    if-eq v3, v5, :cond_5

    if-eq v3, v1, :cond_4

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/4 v5, 0x2

    const/16 v6, 0xd

    if-ne v3, v6, :cond_0

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v4, :cond_0

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-ne v3, v6, :cond_1

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/4 v4, 0x3

    if-lt v3, v4, :cond_1

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0xff

    if-gt v3, v4, :cond_1

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->match(C)V

    goto :goto_3

    :cond_1
    sget-object v3, Lantlr/ANTLRLexer;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual {v3, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    const/16 v3, 0x5d

    if-eqz v2, :cond_2

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->matchNot(C)V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_3

    const/16 p1, 0x3b

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

    :cond_3
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_4
    invoke-virtual {p0, v6}, Lantlr/ANTLRLexer;->mNESTED_ARG_ACTION(Z)V

    goto :goto_0

    :cond_5
    invoke-virtual {p0, v6}, Lantlr/ANTLRLexer;->mCHAR_LITERAL(Z)V

    goto :goto_0

    :cond_6
    invoke-virtual {p0, v6}, Lantlr/ANTLRLexer;->mSTRING_LITERAL(Z)V

    goto/16 :goto_0

    :cond_7
    :goto_2
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_3
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto/16 :goto_0
.end method

.method public final mNOT_OP(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x7e

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mOPEN_ELEMENT_OPTION(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3c

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

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

.method public final mOR(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x7c

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mPLUS(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x2b

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    const/16 p1, 0x2f

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

.method public final mQUESTION(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    const/16 p1, 0x2d

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

.method public final mRANGE(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const-string v1, ".."

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mRCURLY(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x7d

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    const/16 p1, 0x11

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mRULE_REF(Z)V
    .locals 6

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mINTERNAL_RULE_REF(Z)I

    move-result v2

    const/4 v3, 0x1

    const/16 v4, 0x7b

    const/16 v5, 0x33

    if-ne v2, v5, :cond_0

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mWS_LOOP(Z)V

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v4, :cond_1

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    const/16 v2, 0xe

    goto :goto_0

    :cond_0
    const/4 v5, 0x4

    if-ne v2, v5, :cond_1

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mWS_LOOP(Z)V

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v4, :cond_1

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    const/16 v2, 0x17

    :cond_1
    :goto_0
    if-eqz p1, :cond_2

    const/4 p1, -0x1

    if-eq v2, p1, :cond_2

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

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

.method public final mSEMI(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3b

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    const/16 p1, 0x10

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
    sget-object v1, Lantlr/ANTLRLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v1, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    sget-object v1, Lantlr/ANTLRLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Lantlr/collections/impl/BitSet;)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v3, 0xa

    const/16 v4, 0xd

    if-ne v1, v4, :cond_1

    const/4 v1, 0x2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v3, :cond_1

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_1
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_1
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v4, :cond_2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v3, :cond_4

    goto :goto_1

    :goto_2
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    if-eqz p1, :cond_3

    const/16 p1, 0x36

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

    :cond_3
    const/4 p1, 0x0

    :goto_3
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

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
.end method

.method public final mSTAR(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x2a

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    const/16 p1, 0x2e

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

    invoke-virtual {p0, v2}, Lantlr/ANTLRLexer;->mESC(Z)V

    goto :goto_0

    :cond_0
    sget-object v3, Lantlr/ANTLRLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

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

    goto :goto_1

    :cond_2
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mTOKEN_REF(Z)V
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x5a

    const/16 v2, 0x41

    :pswitch_0
    invoke-virtual {p0, v2, v1}, Lantlr/CharScanner;->matchRange(CC)V

    :goto_0
    const/4 v3, 0x1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x5f

    if-eq v3, v4, :cond_1

    packed-switch v3, :pswitch_data_0

    packed-switch v3, :pswitch_data_1

    packed-switch v3, :pswitch_data_2

    const/16 v1, 0x18

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->testLiteralsTable(I)I

    move-result v1

    if-eqz p1, :cond_0

    const/4 p1, -0x1

    if-eq v1, p1, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

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

    :pswitch_1
    const/16 v3, 0x30

    const/16 v4, 0x39

    :goto_1
    invoke-virtual {p0, v3, v4}, Lantlr/CharScanner;->matchRange(CC)V

    goto :goto_0

    :pswitch_2
    const/16 v3, 0x61

    const/16 v4, 0x7a

    goto :goto_1

    :cond_0
    const/4 p1, 0x0

    :goto_2
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_1
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    nop

    :pswitch_data_0
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
.end method

.method public final mTREE_BEGIN(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const-string v1, "#("

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    if-eqz p1, :cond_0

    const/16 p1, 0x2c

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

.method public final mWILDCARD(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x2e

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

    const/16 p1, 0x32

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

.method public final mWS(Z)V
    .locals 3

    iget-object p1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p1}, Lantlr/ANTLRStringBuffer;->length()I

    const/4 p1, 0x1

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    const/16 v1, 0x9

    if-eq v0, v1, :cond_4

    const/16 v1, 0xa

    if-eq v0, v1, :cond_3

    const/16 v2, 0x20

    if-eq v0, v2, :cond_2

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    const/16 v2, 0xd

    if-ne v0, v2, :cond_0

    const/4 v0, 0x2

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    if-ne v0, v1, :cond_0

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    if-ne v0, v2, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    goto :goto_1

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

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_3
    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :goto_1
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto :goto_2

    :cond_4
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :goto_2
    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mWS_LOOP(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0x9

    const/4 v3, 0x0

    if-eq v1, v2, :cond_2

    const/16 v2, 0xa

    if-eq v1, v2, :cond_2

    const/16 v2, 0xd

    if-eq v1, v2, :cond_2

    const/16 v2, 0x20

    if-eq v1, v2, :cond_2

    const/16 v2, 0x2f

    if-eq v1, v2, :cond_1

    if-eqz p1, :cond_0

    const/16 p1, 0x3d

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

    :cond_0
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_1
    invoke-virtual {p0, v3}, Lantlr/ANTLRLexer;->mCOMMENT(Z)V

    goto :goto_0

    :cond_2
    invoke-virtual {p0, v3}, Lantlr/ANTLRLexer;->mWS(Z)V

    goto :goto_0
.end method

.method public final mWS_OPT(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    sget-object v1, Lantlr/ANTLRLexer;->_tokenSet_5:Lantlr/collections/impl/BitSet;

    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual {v1, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/ANTLRLexer;->mWS(Z)V

    :cond_0
    if-eqz p1, :cond_1

    const/16 p1, 0x3f

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

    const/16 p1, 0x3a

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
    .locals 5

    :goto_0
    invoke-virtual {p0}, Lantlr/CharScanner;->resetText()V

    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0x9

    if-eq v1, v2, :cond_8

    const/16 v2, 0xa

    if-eq v1, v2, :cond_8

    const/16 v2, 0xd

    if-eq v1, v2, :cond_8

    const/16 v2, 0x5e

    if-eq v1, v2, :cond_7

    const/16 v2, 0x3e

    if-eq v1, v2, :cond_6

    const/16 v3, 0x3f

    if-eq v1, v3, :cond_5

    packed-switch v1, :pswitch_data_0

    packed-switch v1, :pswitch_data_1

    packed-switch v1, :pswitch_data_2

    packed-switch v1, :pswitch_data_3

    packed-switch v1, :pswitch_data_4

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    goto/16 :goto_1

    :pswitch_0
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mTREE_BEGIN(Z)V

    goto/16 :goto_2

    :pswitch_1
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mSTRING_LITERAL(Z)V

    goto/16 :goto_2

    :pswitch_2
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mBANG(Z)V

    goto/16 :goto_2

    :pswitch_3
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mCOMMA(Z)V

    goto/16 :goto_2

    :pswitch_4
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mPLUS(Z)V

    goto/16 :goto_2

    :pswitch_5
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mSTAR(Z)V

    goto/16 :goto_2

    :pswitch_6
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mRPAREN(Z)V

    goto/16 :goto_2

    :pswitch_7
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mLPAREN(Z)V

    goto/16 :goto_2

    :pswitch_8
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mCHAR_LITERAL(Z)V

    goto/16 :goto_2

    :pswitch_9
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mOPEN_ELEMENT_OPTION(Z)V

    goto/16 :goto_2

    :pswitch_a
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mSEMI(Z)V

    goto/16 :goto_2

    :pswitch_b
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mCOLON(Z)V

    goto/16 :goto_2

    :pswitch_c
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mINT(Z)V

    goto/16 :goto_2

    :pswitch_d
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mCOMMENT(Z)V

    goto/16 :goto_2

    :pswitch_e
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mARG_ACTION(Z)V

    goto/16 :goto_2

    :pswitch_f
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mTOKEN_REF(Z)V

    goto/16 :goto_2

    :pswitch_10
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mNOT_OP(Z)V

    goto/16 :goto_2

    :pswitch_11
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mRCURLY(Z)V

    goto/16 :goto_2

    :pswitch_12
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mOR(Z)V

    goto/16 :goto_2

    :pswitch_13
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mACTION(Z)V

    goto/16 :goto_2

    :pswitch_14
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mRULE_REF(Z)V

    goto/16 :goto_2

    :goto_1
    const/4 v3, 0x2

    const/16 v4, 0x3d

    if-ne v1, v4, :cond_0

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v2, :cond_0

    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mIMPLIES(Z)V

    goto :goto_2

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0x2e

    if-ne v1, v2, :cond_1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v2, :cond_1

    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mRANGE(Z)V

    goto :goto_2

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v4, :cond_2

    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mASSIGN(Z)V

    goto :goto_2

    :cond_2
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v2, :cond_3

    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mWILDCARD(Z)V

    goto :goto_2

    :cond_3
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const v2, 0xffff

    if-ne v1, v2, :cond_4

    invoke-virtual {p0}, Lantlr/CharScanner;->uponEOF()V

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v0

    iput-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    goto :goto_2

    :cond_4
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

    :cond_5
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mQUESTION(Z)V

    goto :goto_2

    :cond_6
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mCLOSE_ELEMENT_OPTION(Z)V

    goto :goto_2

    :cond_7
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mCARET(Z)V

    goto :goto_2

    :cond_8
    :pswitch_15
    invoke-virtual {p0, v0}, Lantlr/ANTLRLexer;->mWS(Z)V

    :goto_2
    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    if-nez v0, :cond_9

    goto/16 :goto_0

    :cond_9
    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getType()I

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

    if-eqz v0, :cond_a

    new-instance v0, Lantlr/TokenStreamIOException;

    check-cast p0, Lantlr/CharStreamIOException;

    iget-object p0, p0, Lantlr/CharStreamIOException;->io:Ljava/io/IOException;

    invoke-direct {v0, p0}, Lantlr/TokenStreamIOException;-><init>(Ljava/io/IOException;)V

    throw v0

    :cond_a
    new-instance v0, Lantlr/TokenStreamException;

    invoke-virtual {p0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Lantlr/TokenStreamException;-><init>(Ljava/lang/String;)V

    throw v0

    :pswitch_data_0
    .packed-switch 0x20
        :pswitch_15
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0x27
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0x2f
        :pswitch_d
        :pswitch_c
        :pswitch_c
        :pswitch_c
        :pswitch_c
        :pswitch_c
        :pswitch_c
        :pswitch_c
        :pswitch_c
        :pswitch_c
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0x41
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_f
        :pswitch_e
    .end packed-switch

    :pswitch_data_4
    .packed-switch 0x61
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_14
        :pswitch_13
        :pswitch_12
        :pswitch_11
        :pswitch_10
    .end packed-switch
.end method
