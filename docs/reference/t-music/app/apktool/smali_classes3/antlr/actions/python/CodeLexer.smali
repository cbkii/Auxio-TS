.class public Lantlr/actions/python/CodeLexer;
.super Lantlr/CharScanner;
.source ""

# interfaces
.implements Lantlr/actions/python/CodeLexerTokenTypes;
.implements Lantlr/TokenStream;


# static fields
.field public static final _tokenSet_0:Lantlr/collections/impl/BitSet;

.field public static final _tokenSet_1:Lantlr/collections/impl/BitSet;


# instance fields
.field public antlrTool:Lantlr/Tool;

.field public lineOffset:I


# direct methods
.method public static constructor <clinit>()V
    .locals 2

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/python/CodeLexer;->mk_tokenSet_0()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/python/CodeLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    new-instance v0, Lantlr/collections/impl/BitSet;

    invoke-static {}, Lantlr/actions/python/CodeLexer;->mk_tokenSet_1()[J

    move-result-object v1

    invoke-direct {v0, v1}, Lantlr/collections/impl/BitSet;-><init>([J)V

    sput-object v0, Lantlr/actions/python/CodeLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    return-void
.end method

.method public constructor <init>(Lantlr/InputBuffer;)V
    .locals 1

    new-instance v0, Lantlr/LexerSharedInputState;

    invoke-direct {v0, p1}, Lantlr/LexerSharedInputState;-><init>(Lantlr/InputBuffer;)V

    invoke-direct {p0, v0}, Lantlr/actions/python/CodeLexer;-><init>(Lantlr/LexerSharedInputState;)V

    return-void
.end method

.method public constructor <init>(Lantlr/LexerSharedInputState;)V
    .locals 0

    invoke-direct {p0, p1}, Lantlr/CharScanner;-><init>(Lantlr/LexerSharedInputState;)V

    const/4 p1, 0x0

    iput p1, p0, Lantlr/actions/python/CodeLexer;->lineOffset:I

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

    invoke-direct {p0, v0}, Lantlr/actions/python/CodeLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public constructor <init>(Ljava/io/Reader;)V
    .locals 1

    new-instance v0, Lantlr/CharBuffer;

    invoke-direct {v0, p1}, Lantlr/CharBuffer;-><init>(Ljava/io/Reader;)V

    invoke-direct {p0, v0}, Lantlr/actions/python/CodeLexer;-><init>(Lantlr/InputBuffer;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;Ljava/lang/String;ILantlr/Tool;)V
    .locals 1

    new-instance v0, Ljava/io/StringReader;

    invoke-direct {v0, p1}, Ljava/io/StringReader;-><init>(Ljava/lang/String;)V

    invoke-direct {p0, v0}, Lantlr/actions/python/CodeLexer;-><init>(Ljava/io/Reader;)V

    invoke-virtual {p0, p3}, Lantlr/CharScanner;->setLine(I)V

    invoke-virtual {p0, p2}, Lantlr/CharScanner;->setFilename(Ljava/lang/String;)V

    iput-object p4, p0, Lantlr/actions/python/CodeLexer;->antlrTool:Lantlr/Tool;

    return-void
.end method

.method public static final mk_tokenSet_0()[J
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

.method public static final mk_tokenSet_1()[J
    .locals 4

    const/16 v0, 0x8

    new-array v0, v0, [J

    const/4 v1, 0x0

    const-wide v2, -0x800000002408L

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


# virtual methods
.method public final mACTION(Z)V
    .locals 4

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x3

    if-lt v2, v3, :cond_0

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0xff

    if-gt v1, v2, :cond_0

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/actions/python/CodeLexer;->mSTUFF(Z)V

    goto :goto_0

    :cond_0
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

    invoke-virtual {p0, v3}, Lantlr/actions/python/CodeLexer;->mSL_COMMENT(Z)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v4, 0x2a

    if-ne v2, v4, :cond_2

    invoke-virtual {p0, v3}, Lantlr/actions/python/CodeLexer;->mML_COMMENT(Z)V

    :goto_0
    if-eqz p1, :cond_1

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

.method public final mIGNWS(Z)V
    .locals 7

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    :goto_0
    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v3, 0xff

    const/4 v4, 0x3

    const/16 v5, 0x20

    const/4 v6, 0x2

    if-ne v2, v5, :cond_0

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v4, :cond_0

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v3, :cond_0

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    :cond_0
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    const/16 v2, 0x9

    if-ne v1, v2, :cond_1

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v4, :cond_1

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v3, :cond_1

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    :cond_1
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

.method public final mML_COMMENT(Z)V
    .locals 10

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    const-string v2, "/*"

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    const-string v2, "#"

    invoke-virtual {v1, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

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

    const-string v3, "# "

    const/4 v5, 0x0

    const/16 v6, 0xa

    const/16 v7, 0xd

    if-ne v2, v7, :cond_1

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v6, :cond_1

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(C)V

    :goto_1
    invoke-virtual {p0, v6}, Lantlr/CharScanner;->match(C)V

    :goto_2
    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    invoke-virtual {p0, v5}, Lantlr/actions/python/CodeLexer;->mIGNWS(Z)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1, v3}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v8, 0xff

    const/4 v9, 0x3

    if-ne v2, v7, :cond_2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v9, :cond_2

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v8, :cond_2

    invoke-virtual {p0, v7}, Lantlr/CharScanner;->match(C)V

    goto :goto_2

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v6, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v9, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-gt v2, v8, :cond_3

    goto :goto_1

    :cond_3
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-lt v2, v9, :cond_4

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v8, :cond_4

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-lt v1, v9, :cond_4

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->LA(I)C

    move-result v1

    if-gt v1, v8, :cond_4

    const v1, 0xffff

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->matchNot(C)V

    goto/16 :goto_0

    :cond_4
    :goto_3
    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    const-string v2, "\n"

    invoke-virtual {v1, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    const-string v2, "*/"

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    if-eqz p1, :cond_5

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

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v1}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v1

    const-string v2, "//"

    invoke-virtual {p0, v2}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    iget-object v2, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v2, v1}, Lantlr/ANTLRStringBuffer;->setLength(I)V

    iget-object v1, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    const-string v2, "#"

    invoke-virtual {v1, v2}, Lantlr/ANTLRStringBuffer;->append(Ljava/lang/String;)V

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

.method public final mSTUFF(Z)V
    .locals 7

    iget-object v0, p0, Lantlr/CharScanner;->text:Lantlr/ANTLRStringBuffer;

    invoke-virtual {v0}, Lantlr/ANTLRStringBuffer;->length()I

    move-result v0

    const/4 v1, 0x1

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/4 v3, 0x2

    const/16 v4, 0x2f

    if-ne v2, v4, :cond_1

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v5, 0x2a

    if-eq v2, v5, :cond_0

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_1

    :cond_0
    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Lantlr/actions/python/CodeLexer;->mCOMMENT(Z)V

    goto :goto_2

    :cond_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    const/16 v5, 0xa

    const/16 v6, 0xd

    if-ne v2, v6, :cond_2

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_2

    const-string v1, "\r\n"

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Ljava/lang/String;)V

    :goto_0
    invoke-virtual {p0}, Lantlr/CharScanner;->newline()V

    goto :goto_2

    :cond_2
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v4, :cond_3

    sget-object v2, Lantlr/actions/python/CodeLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v3}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_3

    invoke-virtual {p0, v4}, Lantlr/CharScanner;->match(C)V

    sget-object v1, Lantlr/actions/python/CodeLexer;->_tokenSet_0:Lantlr/collections/impl/BitSet;

    :goto_1
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->match(Lantlr/collections/impl/BitSet;)V

    goto :goto_2

    :cond_3
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v6, :cond_4

    invoke-virtual {p0, v6}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    :cond_4
    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v2

    if-ne v2, v5, :cond_5

    invoke-virtual {p0, v5}, Lantlr/CharScanner;->match(C)V

    goto :goto_0

    :cond_5
    sget-object v2, Lantlr/actions/python/CodeLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    invoke-virtual {p0, v1}, Lantlr/CharScanner;->LA(I)C

    move-result v3

    invoke-virtual {v2, v3}, Lantlr/collections/impl/BitSet;->member(I)Z

    move-result v2

    if-eqz v2, :cond_7

    sget-object v1, Lantlr/actions/python/CodeLexer;->_tokenSet_1:Lantlr/collections/impl/BitSet;

    goto :goto_1

    :goto_2
    if-eqz p1, :cond_6

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

    :cond_6
    const/4 p1, 0x0

    :goto_3
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

.method public nextToken()Lantlr/Token;
    .locals 2

    :goto_0
    invoke-virtual {p0}, Lantlr/CharScanner;->resetText()V

    const/4 v0, 0x1

    :try_start_0
    invoke-virtual {p0, v0}, Lantlr/actions/python/CodeLexer;->mACTION(Z)V

    iget-object v0, p0, Lantlr/CharScanner;->_returnToken:Lantlr/Token;

    if-nez v0, :cond_0

    goto :goto_0

    :cond_0
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

    goto :goto_1

    :catch_1
    move-exception p0

    :try_start_1
    new-instance v0, Lantlr/TokenStreamRecognitionException;

    invoke-direct {v0, p0}, Lantlr/TokenStreamRecognitionException;-><init>(Lantlr/RecognitionException;)V

    throw v0
    :try_end_1
    .catch Lantlr/CharStreamException; {:try_start_1 .. :try_end_1} :catch_0

    :goto_1
    instance-of v0, p0, Lantlr/CharStreamIOException;

    if-eqz v0, :cond_1

    new-instance v0, Lantlr/TokenStreamIOException;

    check-cast p0, Lantlr/CharStreamIOException;

    iget-object p0, p0, Lantlr/CharStreamIOException;->io:Ljava/io/IOException;

    invoke-direct {v0, p0}, Lantlr/TokenStreamIOException;-><init>(Ljava/io/IOException;)V

    throw v0

    :cond_1
    new-instance v0, Lantlr/TokenStreamException;

    invoke-virtual {p0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Lantlr/TokenStreamException;-><init>(Ljava/lang/String;)V

    throw v0
.end method

.method public reportError(Lantlr/RecognitionException;)V
    .locals 3

    iget-object v0, p0, Lantlr/actions/python/CodeLexer;->antlrTool:Lantlr/Tool;

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

    iget-object v0, p0, Lantlr/actions/python/CodeLexer;->antlrTool:Lantlr/Tool;

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

    iget-object p0, p0, Lantlr/actions/python/CodeLexer;->antlrTool:Lantlr/Tool;

    invoke-virtual {p0, p1}, Lantlr/Tool;->warning(Ljava/lang/String;)V

    goto :goto_0

    :cond_0
    iget-object v0, p0, Lantlr/actions/python/CodeLexer;->antlrTool:Lantlr/Tool;

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
