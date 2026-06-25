.class public Lantlr/ANTLRTokdefLexer;
.super Lantlr/CharScanner;
.source ""

# interfaces
.implements Lantlr/ANTLRTokdefParserTokenTypes;
.implements Lantlr/TokenStream;


# static fields
.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_1:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_2:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_3:Lantlr/collections/impl/BitSet;


# direct methods
.method public static constructor <clinit>()V
    .locals 2

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRTokdefLexer;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRTokdefLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRTokdefLexer;->mk_tokenSet_1()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRTokdefLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRTokdefLexer;->mk_tokenSet_2()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRTokdefLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/ANTLRTokdefLexer;->mk_tokenSet_3()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/ANTLRTokdefLexer;->_tokenSet_3:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 1

    new-instance v0, Lantlr/LexerSharedInputState;

    invoke-direct {v0, p1}, Lantlr/LexerSharedInputState;-><init>(Lantlr/InputBuffer;)V

    invoke-direct {p0, v0}, Lantlr/ANTLRTokdefLexer;-><init>(Lantlr/LexerSharedInputState;)V

    return-void
.end method

.method public constructor <init>(Lantlr/LexerSharedInputState;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/CharScanner;-><init>(Lantlr/LexerSharedInputState;)V

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

    invoke-direct {p0, v0}, Lantlr/ANTLRTokdefLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public constructor <init>(Ljava/io/Reader;)V
    .locals 1

    new-instance v0, Lantlr/CharBuffer;

    invoke-direct {v0, p1}, Lantlr/CharBuffer;-><init>(Ljava/io/Reader;)V

    invoke-direct {p0, v0}, Lantlr/ANTLRTokdefLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
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

    const-wide v2, -0x800000000008L

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

.method public static final mk_tokenSet_2()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x40000000408L

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


# virtual methods
.method public final mASSIGN(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x3d

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
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

    if-eq v3, v4, :cond_8

    const/16 v4, 0x27

    if-eq v3, v4, :cond_8

    if-eq v3, v1, :cond_7

    const/16 v1, 0x62

    if-eq v3, v1, :cond_7

    const/16 v1, 0x66

    if-eq v3, v1, :cond_7

    const/16 v1, 0x6e

    if-eq v3, v1, :cond_7

    const/16 v1, 0x72

    if-eq v3, v1, :cond_7

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

    invoke-virtual {p0, v4}, Lantlr/ANTLRTokdefLexer;->mDIGIT(Z)V

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
    invoke-virtual {p0, v4}, Lantlr/ANTLRTokdefLexer;->mDIGIT(Z)V

    goto :goto_1

    :cond_2
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v8, :cond_3

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v7, :cond_3

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

    if-lt v1, v8, :cond_5

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v7, :cond_5

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
    const/16 v1, 0x75

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0, v4}, Lantlr/ANTLRTokdefLexer;->mXDIGIT(Z)V

    invoke-virtual {p0, v4}, Lantlr/ANTLRTokdefLexer;->mXDIGIT(Z)V

    invoke-virtual {p0, v4}, Lantlr/ANTLRTokdefLexer;->mXDIGIT(Z)V

    invoke-virtual {p0, v4}, Lantlr/ANTLRTokdefLexer;->mXDIGIT(Z)V

    goto :goto_1

    :cond_7
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    goto :goto_1

    :cond_8
    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    :goto_1
    if-eqz p1, :cond_9

    const/16 p1, 0xd

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

.method public final mID(Z)V
    .locals 6

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

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

    :pswitch_0
    const/16 v2, 0x61

    const/16 v3, 0x7a

    goto :goto_1

    :goto_0
    const/4 v2, 0x0

    const/4 v3, 0x4

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    const/16 v5, 0x5f

    if-eq v4, v5, :cond_1

    packed-switch v4, :pswitch_data_2

    packed-switch v4, :pswitch_data_3

    packed-switch v4, :pswitch_data_4

    if-eqz p1, :cond_0

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

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

    :pswitch_1
    const/16 v2, 0x30

    const/16 v3, 0x39

    goto :goto_1

    :pswitch_2
    const/16 v2, 0x41

    const/16 v3, 0x5a

    :goto_1
    invoke-virtual {p0, v2, v3}, Lantlr/CharScanner;->matchRange(CC)V

    goto :goto_0

    :cond_0
    :goto_2
    iput-object v2, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_1
    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    nop

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

    invoke-virtual {p0, v1}, Lantlr/ANTLRTokdefLexer;->mDIGIT(Z)V

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_0
    if-lt v2, v3, :cond_2

    if-eqz p1, :cond_1

    const/16 p1, 0x9

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

.method public final mLPAREN(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x28

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mML_COMMENT(Z)V
    .locals 3

    iget-object p1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p1}, Lantlr/ANTLRStringBuffer;->length()I

    const-string p1, "/*"

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    :goto_0
    const/4 p1, 0x1

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    const/16 v1, 0x2a

    if-ne v0, v1, :cond_0

    sget-object v0, Lantlr/ANTLRTokdefLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    const/4 v2, 0x2

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    invoke-virtual {v0, v2}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    const/16 p1, 0x2f

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->matchNot(C)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result v0

    const/16 v2, 0xa

    if-ne v0, v2, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto :goto_0

    :cond_1
    sget-object v0, Lantlr/ANTLRTokdefLexer;->_tokenSet_2:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result p1

    invoke-virtual {v0, p1}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result p1

    if-eqz p1, :cond_2

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    goto :goto_0

    :cond_2
    const-string p1, "*/"

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    const/4 p1, 0x0

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

    const/16 p1, 0x8

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
    .locals 3

    iget-object p1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {p1}, Lantlr/ANTLRStringBuffer;->length()I

    const-string p1, "//"

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    :goto_0
    sget-object p1, Lantlr/ANTLRTokdefLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    invoke-virtual {p1, v1}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result p1

    if-eqz p1, :cond_0

    sget-object p1, Lantlr/ANTLRTokdefLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->match(Lantlr/collections/impl/BitSet;)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result p1

    const/16 v1, 0xa

    if-eq p1, v1, :cond_2

    const/16 v2, 0xd

    if-ne p1, v2, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result p1

    if-ne p1, v1, :cond_3

    goto :goto_1

    :cond_1
    new-instance p1, Lantlr/NoViableAltForCharException;

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

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
    :goto_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :cond_3
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    const/4 p1, 0x0

    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
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

    invoke-virtual {p0, v2}, Lantlr/ANTLRTokdefLexer;->mESC(Z)V

    goto :goto_0

    :cond_0
    sget-object v3, Lantlr/ANTLRTokdefLexer;->_tokenSet_3:Lantlr/collections/impl/BitSet;

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

    goto :goto_1

    :cond_2
    const/4 p1, 0x0

    :goto_1
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

    if-eq v0, v1, :cond_2

    const/16 v2, 0xd

    if-eq v0, v2, :cond_1

    const/16 v1, 0x20

    if-ne v0, v1, :cond_0

    goto :goto_0

    :cond_0
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

    :cond_1
    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0, p1}, Lantlr/CharScanner;->LA(I)C

    move-result p1

    if-ne p1, v1, :cond_3

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :cond_3
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto :goto_1

    :cond_4
    :goto_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    :goto_1
    const/4 p1, 0x0

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

    if-eq v1, v2, :cond_7

    const/16 v2, 0xa

    if-eq v1, v2, :cond_7

    const/16 v2, 0xd

    if-eq v1, v2, :cond_7

    const/16 v2, 0x20

    if-eq v1, v2, :cond_7

    const/16 v2, 0x22

    if-eq v1, v2, :cond_6

    const/16 v2, 0x3d

    if-eq v1, v2, :cond_5

    const/16 v2, 0x28

    if-eq v1, v2, :cond_4

    const/16 v2, 0x29

    if-eq v1, v2, :cond_3

    packed-switch v1, :pswitch_data_0

    packed-switch v1, :pswitch_data_1

    packed-switch v1, :pswitch_data_2

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    goto :goto_1

    :pswitch_0
    invoke-virtual {p0, v0}, Lantlr/ANTLRTokdefLexer;->mINT(Z)V

    goto :goto_2

    :pswitch_1
    invoke-virtual {p0, v0}, Lantlr/ANTLRTokdefLexer;->mID(Z)V

    goto :goto_2

    :goto_1
    const/4 v2, 0x2

    const/16 v3, 0x2f

    if-ne v1, v3, :cond_0

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v3, :cond_0

    invoke-virtual {p0, v0}, Lantlr/ANTLRTokdefLexer;->mSL_COMMENT(Z)V

    goto :goto_2

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v3, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0x2a

    if-ne v1, v2, :cond_1

    invoke-virtual {p0, v0}, Lantlr/ANTLRTokdefLexer;->mML_COMMENT(Z)V

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
    invoke-virtual {p0, v0}, Lantlr/ANTLRTokdefLexer;->mRPAREN(Z)V

    goto :goto_2

    :cond_4
    invoke-virtual {p0, v0}, Lantlr/ANTLRTokdefLexer;->mLPAREN(Z)V

    goto :goto_2

    :cond_5
    invoke-virtual {p0, v0}, Lantlr/ANTLRTokdefLexer;->mASSIGN(Z)V

    goto :goto_2

    :cond_6
    invoke-virtual {p0, v0}, Lantlr/ANTLRTokdefLexer;->mSTRING(Z)V

    goto :goto_2

    :cond_7
    invoke-virtual {p0, v0}, Lantlr/ANTLRTokdefLexer;->mWS(Z)V

    :goto_2
    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    if-nez v0, :cond_8

    goto/16 :goto_0

    :cond_8
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

    if-eqz v0, :cond_9

    new-instance v0, Lantlr/TokenStreamIOException;

    check-cast p0, Lantlr/CharStreamIOException;

    iget-object p0, p0, Lantlr/CharStreamIOException;->io:Ljava/io/IOException;

    invoke-direct {v0, p0}, Lantlr/TokenStreamIOException;-><init>(Ljava/io/IOException;)V

    throw v0

    :cond_9
    new-instance v0, Lantlr/TokenStreamException;

    invoke-virtual {p0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Lantlr/TokenStreamException;-><init>(Ljava/lang/String;)V

    throw v0

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
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
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
