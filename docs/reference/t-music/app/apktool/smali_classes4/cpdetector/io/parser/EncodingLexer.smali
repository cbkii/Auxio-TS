.class public Lcpdetector/io/parser/EncodingLexer;
.super Lantlr/CharScanner;
.source ""

# interfaces
.implements Lcpdetector/io/parser/EncodingParserTokenTypes;
.implements Lantlr/TokenStream;


# static fields
.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;


# direct methods
.method public static constructor <clinit>()V
    .locals 2

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lcpdetector/io/parser/EncodingLexer;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lcpdetector/io/parser/EncodingLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 1

    new-instance v0, Lantlr/LexerSharedInputState;

    invoke-direct {v0, p1}, Lantlr/LexerSharedInputState;-><init>(Lantlr/InputBuffer;)V

    invoke-direct {p0, v0}, Lcpdetector/io/parser/EncodingLexer;-><init>(Lantlr/LexerSharedInputState;)V

    return-void
.end method

.method public constructor <init>(Lantlr/LexerSharedInputState;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/CharScanner;-><init>(Lantlr/LexerSharedInputState;)V

    const/4 p1, 0x1

    iput-boolean p1, p0, Lantlr/CharScanner;->caseSensitiveLiterals:Z

    const/4 p1, 0x0

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

    invoke-direct {p0, v0}, Lcpdetector/io/parser/EncodingLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public constructor <init>(Ljava/io/Reader;)V
    .locals 1

    new-instance v0, Lantlr/CharBuffer;

    invoke-direct {v0, p1}, Lantlr/CharBuffer;-><init>(Ljava/io/Reader;)V

    invoke-direct {p0, v0}, Lcpdetector/io/parser/EncodingLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public static final mk_tokenSet_0()[J
    .locals 4

    const/16 v0, 0x401

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, 0x100002400L    # 2.1220003443E-314

    aput-wide v2, v0, v1

    const/4 v1, 0x1

    const-wide v2, 0x200000000000L

    aput-wide v2, v0, v1

    return-object v0
.end method


# virtual methods
.method public final mDIGIT(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x30

    const/16 v2, 0x39

    invoke-virtual {p0, v1, v2}, Lantlr/CharScanner;->matchRange(CC)V

    if-eqz p1, :cond_0

    const/16 p1, 0xa

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

.method public final mIDENTIFIER(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x0

    :pswitch_0
    invoke-virtual {p0, v1}, Lcpdetector/io/parser/EncodingLexer;->mLETTER(Z)V

    :goto_0
    const/4 v2, 0x1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0x2d

    if-eq v2, v3, :cond_1

    const/16 v3, 0x2e

    if-eq v2, v3, :cond_1

    const/16 v3, 0x5f

    if-eq v2, v3, :cond_1

    packed-switch v2, :pswitch_data_0

    packed-switch v2, :pswitch_data_1

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

    goto :goto_1

    :pswitch_1
    invoke-virtual {p0, v1}, Lcpdetector/io/parser/EncodingLexer;->mDIGIT(Z)V

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_1
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

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

.method public final mLETTER(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x61

    const/16 v2, 0x7a

    invoke-virtual {p0, v1, v2}, Lantlr/CharScanner;->matchRange(CC)V

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

.method public final mMETA_CONTENT_TYPE(Z)V
    .locals 12

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    const/16 v2, 0x3c

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x0

    const/16 v4, 0x20

    const/16 v5, 0xd

    const/16 v6, 0xa

    if-eq v2, v6, :cond_1

    if-eq v2, v5, :cond_1

    if-eq v2, v4, :cond_1

    const/16 v7, 0x6d

    if-ne v2, v7, :cond_0

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
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v7, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v7, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_0
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const-string v7, "meta"

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v7, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v7, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_3

    if-eq v2, v5, :cond_3

    if-eq v2, v4, :cond_3

    const/16 v7, 0x68

    if-ne v2, v7, :cond_2

    goto :goto_1

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
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v7, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v7, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_1
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const-string v7, "http-equiv"

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v7, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v7, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v7, 0x3d

    if-eq v2, v6, :cond_5

    if-eq v2, v5, :cond_5

    if-eq v2, v4, :cond_5

    if-ne v2, v7, :cond_4

    goto :goto_2

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
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v8, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v8, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_2
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(C)V

    iget-object v8, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v8, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v8, 0x63

    const/16 v9, 0x22

    if-eq v2, v6, :cond_7

    if-eq v2, v5, :cond_7

    if-eq v2, v4, :cond_7

    if-eq v2, v9, :cond_8

    if-ne v2, v8, :cond_6

    goto :goto_3

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
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_8
    :goto_3
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v9, :cond_a

    if-ne v2, v8, :cond_9

    goto :goto_4

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
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_c

    if-eq v2, v5, :cond_c

    if-eq v2, v4, :cond_c

    if-ne v2, v8, :cond_b

    goto :goto_4

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
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_4
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const-string v10, "content-type"

    invoke-virtual {p0, v10}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_e

    if-eq v2, v5, :cond_e

    if-eq v2, v4, :cond_e

    if-eq v2, v9, :cond_f

    if-ne v2, v8, :cond_d

    goto :goto_5

    :cond_d
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

    :cond_e
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_f
    :goto_5
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v9, :cond_11

    if-ne v2, v8, :cond_10

    goto :goto_6

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
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_13

    if-eq v2, v5, :cond_13

    if-eq v2, v4, :cond_13

    if-ne v2, v8, :cond_12

    goto :goto_6

    :cond_12
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

    :cond_13
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_6
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const-string v10, "content"

    invoke-virtual {p0, v10}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_15

    if-eq v2, v5, :cond_15

    if-eq v2, v4, :cond_15

    if-ne v2, v7, :cond_14

    goto :goto_7

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

    :cond_15
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_7
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(C)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v10, 0x3b

    if-eq v2, v6, :cond_16

    if-eq v2, v5, :cond_16

    if-eq v2, v4, :cond_16

    if-eq v2, v9, :cond_17

    if-eq v2, v10, :cond_17

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

    :cond_16
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v11, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_17
    :pswitch_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v9, :cond_18

    if-eq v2, v10, :cond_1a

    packed-switch v2, :pswitch_data_2

    packed-switch v2, :pswitch_data_3

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

    :cond_18
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    iget-object v11, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_19

    if-eq v2, v5, :cond_19

    if-eq v2, v4, :cond_19

    if-eq v2, v10, :cond_1a

    packed-switch v2, :pswitch_data_4

    packed-switch v2, :pswitch_data_5

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

    :cond_19
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v11, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_1a
    :pswitch_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    packed-switch v2, :pswitch_data_6

    packed-switch v2, :pswitch_data_7

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v10}, Lantlr/CharScanner;->match(C)V

    iget-object v10, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_1c

    if-eq v2, v5, :cond_1c

    if-eq v2, v4, :cond_1c

    if-ne v2, v8, :cond_1b

    goto/16 :goto_8

    :pswitch_2
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mDIGIT(Z)V

    iget-object v11, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_19

    if-eq v2, v5, :cond_19

    if-eq v2, v4, :cond_19

    if-eq v2, v10, :cond_1a

    packed-switch v2, :pswitch_data_8

    packed-switch v2, :pswitch_data_9

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

    :pswitch_3
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const/16 v11, 0x2f

    invoke-virtual {p0, v11}, Lantlr/CharScanner;->match(C)V

    iget-object v11, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_19

    if-eq v2, v5, :cond_19

    if-eq v2, v4, :cond_19

    if-eq v2, v10, :cond_1a

    packed-switch v2, :pswitch_data_a

    packed-switch v2, :pswitch_data_b

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

    :pswitch_4
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mLETTER(Z)V

    iget-object v11, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v11, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_19

    if-eq v2, v5, :cond_19

    if-eq v2, v4, :cond_19

    if-eq v2, v10, :cond_1a

    packed-switch v2, :pswitch_data_c

    packed-switch v2, :pswitch_data_d

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

    :cond_1b
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

    :cond_1c
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v8, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v8, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_8
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const-string v8, "charset"

    invoke-virtual {p0, v8}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v8, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v8, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_1e

    if-eq v2, v5, :cond_1e

    if-eq v2, v4, :cond_1e

    if-ne v2, v7, :cond_1d

    goto :goto_9

    :cond_1d
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

    :cond_1e
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v8, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v8, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_9
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(C)V

    iget-object v7, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v7, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v5, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_20

    :cond_1f
    :goto_a
    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v7, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v7, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_20
    const/4 v2, 0x4

    const/4 v7, 0x0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    const/16 v10, 0x2d

    if-eq v8, v10, :cond_25

    const/16 v10, 0x5f

    if-eq v8, v10, :cond_24

    packed-switch v8, :pswitch_data_e

    packed-switch v8, :pswitch_data_f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-ne v8, v9, :cond_22

    iget-object v8, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v8}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v8

    invoke-virtual {p0, v9}, Lantlr/CharScanner;->match(C)V

    iget-object v9, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v9, v8}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v8

    if-eq v8, v6, :cond_21

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v6

    if-eq v6, v5, :cond_21

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v4, :cond_22

    goto :goto_b

    :pswitch_5
    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mDIGIT(Z)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v5, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_20

    goto :goto_a

    :pswitch_6
    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mLETTER(Z)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v5, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_20

    goto :goto_a

    :cond_21
    :goto_b
    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v3, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_22
    if-eqz p1, :cond_23

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v7

    new-instance p1, Ljava/lang/String;

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->getBuffer()[C

    move-result-object v1

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    sub-int/2addr v2, v0

    invoke-direct {p1, v1, v0, v2}, Ljava/lang/String;-><init>([CII)V

    invoke-virtual {v7, p1}, Lantlr/Token;->setText(Ljava/lang/String;)V

    :cond_23
    iput-object v7, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void

    :cond_24
    invoke-virtual {p0, v10}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v5, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_20

    goto/16 :goto_a

    :cond_25
    invoke-virtual {p0, v10}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v6, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-eq v2, v5, :cond_1f

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_20

    goto/16 :goto_a

    :pswitch_data_0
    .packed-switch 0x2f
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
    .packed-switch 0x2f
        :pswitch_1
        :pswitch_1
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

    :pswitch_data_4
    .packed-switch 0x2f
        :pswitch_1
        :pswitch_1
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

    :pswitch_data_6
    .packed-switch 0x2f
        :pswitch_3
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

    :pswitch_data_7
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
    .end packed-switch

    :pswitch_data_8
    .packed-switch 0x2f
        :pswitch_1
        :pswitch_1
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

    :pswitch_data_a
    .packed-switch 0x2f
        :pswitch_1
        :pswitch_1
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

    :pswitch_data_b
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

    :pswitch_data_c
    .packed-switch 0x2f
        :pswitch_1
        :pswitch_1
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

    :pswitch_data_d
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

    :pswitch_data_e
    .packed-switch 0x30
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
    .end packed-switch

    :pswitch_data_f
    .packed-switch 0x61
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
        :pswitch_6
    .end packed-switch
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

    if-eq v2, v3, :cond_1

    const/16 v4, 0xd

    if-ne v2, v4, :cond_0

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

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
    :goto_0
    invoke-virtual {p0, v3}, Lantlr/CharScanner;->match(C)V

    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    if-eqz p1, :cond_2

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

    goto :goto_1

    :cond_2
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mSPACE(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/16 v1, 0x20

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(C)V

    if-eqz p1, :cond_0

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

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mSPACING(Z)V
    .locals 5

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x0

    const/16 v4, 0xa

    if-eq v2, v4, :cond_1

    const/16 v4, 0xd

    if-eq v2, v4, :cond_1

    const/16 v4, 0x20

    if-ne v2, v4, :cond_0

    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mSPACE(Z)V

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
    invoke-virtual {p0, v3}, Lcpdetector/io/parser/EncodingLexer;->mNEWLINE(Z)V

    :goto_0
    if-eqz p1, :cond_2

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

    goto :goto_1

    :cond_2
    const/4 p1, 0x0

    :goto_1
    iput-object p1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-void
.end method

.method public final mXML_ENCODING_DECL(Z)V
    .locals 16

    move-object/from16 v0, p0

    iget-object v1, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    const-string v3, "<?xml"

    invoke-virtual {v0, v3}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    const/4 v2, 0x1

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x76

    const/16 v5, 0x65

    const/16 v6, 0x20

    const/16 v7, 0xd

    const/16 v8, 0xa

    const/4 v9, 0x0

    if-eq v3, v8, :cond_1

    if-eq v3, v7, :cond_1

    if-eq v3, v6, :cond_1

    if-eq v3, v5, :cond_2

    if-ne v3, v4, :cond_0

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
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v10, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v10, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_2
    :goto_0
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v10, 0x3d

    const-string v11, "="

    const-string v12, "\'"

    const/16 v13, 0x27

    const/16 v14, 0x22

    if-eq v3, v5, :cond_1a

    if-ne v3, v4, :cond_19

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    const-string v4, "version"

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_4

    if-eq v3, v7, :cond_4

    if-eq v3, v6, :cond_4

    if-ne v3, v10, :cond_3

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
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_1
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v11}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_6

    if-eq v3, v7, :cond_6

    if-eq v3, v6, :cond_6

    if-eq v3, v14, :cond_7

    if-ne v3, v13, :cond_5

    goto :goto_2

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
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_7
    :goto_2
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    const/16 v4, 0x2e

    if-eq v3, v14, :cond_10

    if-ne v3, v13, :cond_f

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v12}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_8

    if-eq v3, v7, :cond_8

    if-eq v3, v6, :cond_8

    packed-switch v3, :pswitch_data_0

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
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :pswitch_0
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mDIGIT(Z)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_a

    if-eq v3, v7, :cond_a

    if-eq v3, v6, :cond_a

    if-ne v3, v4, :cond_9

    goto :goto_3

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
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_3
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->match(C)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_b

    if-eq v3, v7, :cond_b

    if-eq v3, v6, :cond_b

    packed-switch v3, :pswitch_data_1

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
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :pswitch_1
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mDIGIT(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_d

    if-eq v3, v7, :cond_d

    if-eq v3, v6, :cond_d

    if-ne v3, v13, :cond_c

    goto :goto_4

    :cond_c
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

    :cond_d
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_4
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v12}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_18

    if-eq v3, v7, :cond_18

    if-eq v3, v6, :cond_18

    if-ne v3, v5, :cond_e

    goto/16 :goto_7

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

    :cond_10
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->match(C)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_11

    if-eq v3, v7, :cond_11

    if-eq v3, v6, :cond_11

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

    :cond_11
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :pswitch_2
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mDIGIT(Z)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_13

    if-eq v3, v7, :cond_13

    if-eq v3, v6, :cond_13

    if-ne v3, v4, :cond_12

    goto :goto_5

    :cond_12
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

    :cond_13
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v15, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v15, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_5
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->match(C)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_14

    if-eq v3, v7, :cond_14

    if-eq v3, v6, :cond_14

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

    :cond_14
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :pswitch_3
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mDIGIT(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_16

    if-eq v3, v7, :cond_16

    if-eq v3, v6, :cond_16

    if-ne v3, v14, :cond_15

    goto :goto_6

    :cond_15
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
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_6
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->match(C)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_18

    if-eq v3, v7, :cond_18

    if-eq v3, v6, :cond_18

    if-ne v3, v5, :cond_17

    goto :goto_7

    :cond_17
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

    :cond_18
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    goto :goto_7

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
    :goto_7
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    const-string v4, "encoding"

    invoke-virtual {v0, v4}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_1c

    if-eq v3, v7, :cond_1c

    if-eq v3, v6, :cond_1c

    if-ne v3, v10, :cond_1b

    goto :goto_8

    :cond_1b
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

    :cond_1c
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_8
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v11}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_1e

    if-eq v3, v7, :cond_1e

    if-eq v3, v6, :cond_1e

    if-eq v3, v14, :cond_1f

    if-ne v3, v13, :cond_1d

    goto :goto_9

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

    :cond_1e
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_1f
    :goto_9
    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v14, :cond_24

    if-ne v3, v13, :cond_23

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v12}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_20

    if-eq v3, v7, :cond_20

    if-eq v3, v6, :cond_20

    packed-switch v3, :pswitch_data_4

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

    :cond_20
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :pswitch_4
    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mIDENTIFIER(Z)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_22

    if-eq v3, v7, :cond_22

    if-eq v3, v6, :cond_22

    if-ne v3, v13, :cond_21

    goto :goto_a

    :cond_21
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

    :cond_22
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_a
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v12}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_28

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v7, :cond_28

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v6, :cond_29

    goto/16 :goto_c

    :cond_23
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

    :cond_24
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->match(C)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_25

    if-eq v3, v7, :cond_25

    if-eq v3, v6, :cond_25

    packed-switch v3, :pswitch_data_5

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

    :cond_25
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :pswitch_5
    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mIDENTIFIER(Z)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_27

    if-eq v3, v7, :cond_27

    if-eq v3, v6, :cond_27

    if-ne v3, v14, :cond_26

    goto :goto_b

    :cond_26
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

    :cond_27
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :goto_b
    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v3

    invoke-virtual {v0, v14}, Lantlr/CharScanner;->match(C)V

    iget-object v4, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v4, v3}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v8, :cond_28

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    if-eq v3, v7, :cond_28

    invoke-virtual {v0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v6, :cond_29

    :cond_28
    :goto_c
    iget-object v2, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v2

    invoke-virtual {v0, v9}, Lcpdetector/io/parser/EncodingLexer;->mSPACING(Z)V

    iget-object v3, v0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v3, v2}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    :cond_29
    if-eqz p1, :cond_2a

    const/4 v2, 0x5

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

    goto :goto_d

    :cond_2a
    const/4 v2, 0x0

    :goto_d
    iput-object v2, v0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

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

    :pswitch_data_2
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

    :pswitch_data_3
    .packed-switch 0x30
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

    :pswitch_data_4
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
    .end packed-switch

    :pswitch_data_5
    .packed-switch 0x61
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
        :pswitch_5
    .end packed-switch
.end method

.method public nextToken()Lantlr/Token;
    .locals 5

    :goto_0
    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->setCommitToPath(Z)V

    invoke-virtual {p0}, Lantlr/CharScanner;->resetText()V

    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/4 v2, 0x2

    const/16 v3, 0x3c

    if-ne v1, v3, :cond_0

    sget-object v1, Lcpdetector/io/parser/EncodingLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v4

    invoke-virtual {v1, v4}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-virtual {p0, v0}, Lcpdetector/io/parser/EncodingLexer;->mMETA_CONTENT_TYPE(Z)V

    goto :goto_1

    :cond_0
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-ne v1, v3, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0x3f

    if-ne v1, v2, :cond_1

    invoke-virtual {p0, v0}, Lcpdetector/io/parser/EncodingLexer;->mXML_ENCODING_DECL(Z)V

    goto :goto_1

    :cond_1
    invoke-virtual {p0, v0}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const v2, 0xffff

    if-ne v1, v2, :cond_3

    invoke-virtual {p0}, Lantlr/CharScanner;->uponEOF()V

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->makeToken(I)Lantlr/Token;

    move-result-object v0

    iput-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    :goto_1
    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    if-nez v0, :cond_2

    goto :goto_0

    :cond_2
    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v0}, Lantlr/Token;->getType()I

    move-result v0

    invoke-virtual {p0, v0}, Lantlr/CharScanner;->testLiteralsTable(I)I

    move-result v0

    iget-object v1, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    invoke-virtual {v1, v0}, Lantlr/Token;->setType(I)V

    iget-object p0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    return-object p0

    :cond_3
    invoke-virtual {p0}, Lantlr/CharScanner;->consume()V
    :try_end_0
    .catch Lantlr/RecognitionException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Lantlr/CharStreamException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception p0

    goto :goto_2

    :catch_1
    move-exception v0

    :try_start_1
    invoke-virtual {p0}, Lantlr/CharScanner;->getCommitToPath()Z

    move-result v1

    if-nez v1, :cond_4

    invoke-virtual {p0}, Lantlr/CharScanner;->consume()V

    goto :goto_0

    :cond_4
    new-instance p0, Lantlr/TokenStreamRecognitionException;

    invoke-direct {p0, v0}, Lantlr/TokenStreamRecognitionException;-><init>(Lantlr/RecognitionException;)V

    throw p0
    :try_end_1
    .catch Lantlr/CharStreamException; {:try_start_1 .. :try_end_1} :catch_0

    :goto_2
    instance-of v0, p0, Lantlr/CharStreamIOException;

    if-eqz v0, :cond_5

    new-instance v0, Lantlr/TokenStreamIOException;

    check-cast p0, Lantlr/CharStreamIOException;

    iget-object p0, p0, Lantlr/CharStreamIOException;->io:Ljava/io/IOException;

    invoke-direct {v0, p0}, Lantlr/TokenStreamIOException;-><init>(Ljava/io/IOException;)V

    throw v0

    :cond_5
    new-instance v0, Lantlr/TokenStreamException;

    invoke-virtual {p0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Lantlr/TokenStreamException;-><init>(Ljava/lang/String;)V

    throw v0
.end method
