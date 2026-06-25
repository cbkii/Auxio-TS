.class public Lcpdetector/io/ByteOrderMarkDetector;
.super Lcpdetector/io/AbstractCodepageDetector;
.source ""

# interfaces
.implements Lcpdetector/io/ICodepageDetector;


# static fields
.field public static final serialVersionUID:J = 0x3239313438323432L


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lcpdetector/io/AbstractCodepageDetector;-><init>()V

    return-void
.end method


# virtual methods
.method public detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;
    .locals 6

    const-string p0, "UCS-4LE"

    const-string p2, "utf-8"

    const-string v0, "UCS-4BE"

    invoke-static {}, Lcpdetector/io/UnknownCharset;->getInstance()Ljava/nio/charset/Charset;

    move-result-object v1

    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result v2

    const/16 v3, 0xff

    const/16 v4, 0xfe

    const-string v5, "UCS-4"

    if-eqz v2, :cond_b

    const/16 v0, 0xef

    if-eq v2, v0, :cond_8

    if-eq v2, v4, :cond_4

    if-eq v2, v3, :cond_0

    return-object v1

    :cond_0
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p2

    if-eq p2, v4, :cond_1

    return-object v1

    :cond_1
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p2

    const-string v0, "UTF-16LE"

    if-eqz p2, :cond_2

    :try_start_0
    invoke-static {v0}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_0
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    invoke-static {v0}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_0
    return-object p0

    :cond_2
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p1

    if-eqz p1, :cond_3

    :try_start_1
    invoke-static {v0}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_1
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_1 .. :try_end_1} :catch_1

    goto :goto_1

    :catch_1
    invoke-static {v0}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_1
    return-object p0

    :cond_3
    :try_start_2
    invoke-static {p0}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_2
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_2 .. :try_end_2} :catch_2

    goto :goto_2

    :catch_2
    invoke-static {p0}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_2
    return-object p0

    :cond_4
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p0

    if-eq p0, v3, :cond_5

    return-object v1

    :cond_5
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p0

    const-string p2, "UTF-16BE"

    if-eqz p0, :cond_6

    :try_start_3
    invoke-static {p2}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_3
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_3 .. :try_end_3} :catch_3

    goto :goto_3

    :catch_3
    invoke-static {p2}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_3
    return-object p0

    :cond_6
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p0

    if-eqz p0, :cond_7

    :try_start_4
    invoke-static {p2}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_4
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_4 .. :try_end_4} :catch_4

    goto :goto_4

    :catch_4
    invoke-static {p2}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_4
    return-object p0

    :cond_7
    :try_start_5
    invoke-static {v5}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_5
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_5 .. :try_end_5} :catch_5

    goto :goto_5

    :catch_5
    invoke-static {v5}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_5
    return-object p0

    :cond_8
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p0

    const/16 v0, 0xbb

    if-eq p0, v0, :cond_9

    return-object v1

    :cond_9
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p0

    const/16 p1, 0xbf

    if-eq p0, p1, :cond_a

    return-object v1

    :cond_a
    :try_start_6
    invoke-static {p2}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_6
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_6 .. :try_end_6} :catch_6

    goto :goto_6

    :catch_6
    invoke-static {p2}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_6
    return-object p0

    :cond_b
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p0

    if-eqz p0, :cond_c

    return-object v1

    :cond_c
    invoke-virtual {p1}, Ljava/io/InputStream;->read()I

    move-result p0

    if-eq p0, v4, :cond_e

    if-eq p0, v3, :cond_d

    return-object v1

    :cond_d
    :try_start_7
    invoke-static {v5}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_7
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_7 .. :try_end_7} :catch_7

    goto :goto_7

    :catch_7
    invoke-static {v5}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_7
    return-object p0

    :cond_e
    :try_start_8
    invoke-static {v0}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0
    :try_end_8
    .catch Ljava/nio/charset/UnsupportedCharsetException; {:try_start_8 .. :try_end_8} :catch_8

    goto :goto_8

    :catch_8
    invoke-static {v0}, Lcpdetector/io/UnsupportedCharset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :goto_8
    return-object p0
.end method

.method public detectCodepage(Ljava/net/URL;)Ljava/nio/charset/Charset;
    .locals 1

    new-instance v0, Ljava/io/BufferedInputStream;

    invoke-virtual {p1}, Ljava/net/URL;->openStream()Ljava/io/InputStream;

    move-result-object p1

    invoke-direct {v0, p1}, Ljava/io/BufferedInputStream;-><init>(Ljava/io/InputStream;)V

    const p1, 0x7fffffff

    invoke-virtual {p0, v0, p1}, Lcpdetector/io/ByteOrderMarkDetector;->detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;

    move-result-object p0

    invoke-virtual {v0}, Ljava/io/BufferedInputStream;->close()V

    return-object p0
.end method
