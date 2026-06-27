.class public Lcpdetector/io/ParsingDetector;
.super Lcpdetector/io/AbstractCodepageDetector;
.source ""


# static fields
.field public static final serialVersionUID:J = 0x3239313438323432L


# instance fields
.field public m_verbose:Z


# direct methods
.method public constructor <init>()V
    .locals 1

    const/4 v0, 0x0

    invoke-direct {p0, v0}, Lcpdetector/io/ParsingDetector;-><init>(Z)V

    return-void
.end method

.method public constructor <init>(Z)V
    .locals 1

    invoke-direct {p0}, Lcpdetector/io/AbstractCodepageDetector;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcpdetector/io/ParsingDetector;->m_verbose:Z

    iput-boolean p1, p0, Lcpdetector/io/ParsingDetector;->m_verbose:Z

    return-void
.end method


# virtual methods
.method public detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;
    .locals 4

    new-instance v0, Lcpdetector/io/LimitedInputStream;

    invoke-direct {v0, p1, p2}, Lcpdetector/io/LimitedInputStream;-><init>(Ljava/io/InputStream;I)V

    iget-boolean p1, p0, Lcpdetector/io/ParsingDetector;->m_verbose:Z

    if-eqz p1, :cond_0

    sget-object p1, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string p2, "  parsing for html-charset/xml-encoding attribute with codepage: US-ASCII"

    invoke-virtual {p1, p2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_0
    const/4 p1, 0x0

    :try_start_0
    new-instance p2, Lcpdetector/io/parser/EncodingLexer;

    new-instance v1, Ljava/io/InputStreamReader;

    const-string v2, "US-ASCII"

    invoke-direct {v1, v0, v2}, Ljava/io/InputStreamReader;-><init>(Ljava/io/InputStream;Ljava/lang/String;)V

    invoke-direct {p2, v1}, Lcpdetector/io/parser/EncodingLexer;-><init>(Ljava/io/Reader;)V

    new-instance v0, Lcpdetector/io/parser/EncodingParser;

    invoke-direct {v0, p2}, Lcpdetector/io/parser/EncodingParser;-><init>(Lantlr/TokenStream;)V

    invoke-virtual {v0}, Lcpdetector/io/parser/EncodingParser;->htmlDocument()Ljava/lang/String;

    move-result-object p2
    :try_end_0
    .catch Lantlr/ANTLRException; {:try_start_0 .. :try_end_0} :catch_3
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_2

    if-eqz p2, :cond_1

    :try_start_1
    invoke-static {p2}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_1
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_1 .. :try_end_1} :catch_0
    .catch Lantlr/ANTLRException; {:try_start_1 .. :try_end_1} :catch_3
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    :goto_0
    move-object p1, p0

    goto :goto_2

    :catch_0
    :try_start_2
    invoke-static {p2}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    goto :goto_0

    :cond_1
    invoke-static {}, Lcpdetector/io/UnknownCharset;->getInstance()Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_2
    .catch Lantlr/ANTLRException; {:try_start_2 .. :try_end_2} :catch_3
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_1

    goto :goto_0

    :catch_1
    move-exception p1

    move-object v3, p2

    move-object p2, p1

    move-object p1, v3

    goto :goto_1

    :catch_2
    move-exception p2

    :goto_1
    iget-boolean p0, p0, Lcpdetector/io/ParsingDetector;->m_verbose:Z

    if-eqz p0, :cond_2

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v0, "  Decoding Exception: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p2}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, " (unsupported java charset)."

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_2
    if-eqz p1, :cond_3

    invoke-static {p1}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p1

    goto :goto_2

    :cond_3
    invoke-static {}, Lcpdetector/io/UnknownCharset;->getInstance()Ljava/nio/charset/Charset;

    move-result-object p1

    goto :goto_2

    :catch_3
    move-exception p2

    iget-boolean p0, p0, Lcpdetector/io/ParsingDetector;->m_verbose:Z

    if-eqz p0, :cond_4

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    const-string v0, "  ANTLR parser exception: "

    invoke-static {v0}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p2}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p0, p2}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    :cond_4
    :goto_2
    return-object p1
.end method
