.class public Lcpdetector/io/UnicodeDetector;
.super Lcpdetector/io/AbstractCodepageDetector;
.source ""


# static fields
.field public static instance:Lcpdetector/io/ICodepageDetector;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lcpdetector/io/AbstractCodepageDetector;-><init>()V

    return-void
.end method

.method public static getInstance()Lcpdetector/io/ICodepageDetector;
    .locals 1

    sget-object v0, Lcpdetector/io/UnicodeDetector;->instance:Lcpdetector/io/ICodepageDetector;

    if-nez v0, :cond_0

    new-instance v0, Lcpdetector/io/UnicodeDetector;

    invoke-direct {v0}, Lcpdetector/io/UnicodeDetector;-><init>()V

    sput-object v0, Lcpdetector/io/UnicodeDetector;->instance:Lcpdetector/io/ICodepageDetector;

    :cond_0
    sget-object v0, Lcpdetector/io/UnicodeDetector;->instance:Lcpdetector/io/ICodepageDetector;

    return-object v0
.end method


# virtual methods
.method public detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;
    .locals 5

    const/4 p0, 0x4

    new-array p2, p0, [B

    const/4 v0, 0x0

    invoke-virtual {p1, p2, v0, p0}, Ljava/io/InputStream;->read([BII)I

    aget-byte p0, p2, v0

    const/4 p1, -0x2

    const/4 v1, -0x1

    const/4 v2, 0x2

    const/4 v3, 0x1

    if-nez p0, :cond_0

    aget-byte p0, p2, v3

    if-nez p0, :cond_0

    aget-byte p0, p2, v2

    if-ne p0, p1, :cond_0

    aget-byte p0, p2, v2

    if-ne p0, v1, :cond_0

    const-string p0, "UTF-32BE"

    :goto_0
    invoke-static {p0}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    return-object p0

    :cond_0
    aget-byte p0, p2, v0

    if-ne p0, v1, :cond_1

    aget-byte p0, p2, v3

    if-ne p0, p1, :cond_1

    aget-byte p0, p2, v2

    if-nez p0, :cond_1

    aget-byte p0, p2, v2

    if-nez p0, :cond_1

    const-string p0, "UTF-32LE"

    goto :goto_0

    :cond_1
    aget-byte p0, p2, v0

    const/16 v4, -0x11

    if-ne p0, v4, :cond_2

    aget-byte p0, p2, v3

    const/16 v4, -0x45

    if-ne p0, v4, :cond_2

    aget-byte p0, p2, v2

    const/16 v4, -0x41

    if-ne p0, v4, :cond_2

    const-string p0, "UTF-8"

    goto :goto_0

    :cond_2
    aget-byte p0, p2, v0

    if-ne p0, v1, :cond_3

    aget-byte p0, p2, v3

    if-ne p0, p1, :cond_3

    const-string p0, "UTF-16LE"

    goto :goto_0

    :cond_3
    aget-byte p0, p2, v0

    if-ne p0, p1, :cond_4

    aget-byte p0, p2, v3

    if-ne p0, v1, :cond_4

    const-string p0, "UTF-16BE"

    goto :goto_0

    :cond_4
    aget-byte p0, p2, v0

    if-nez p0, :cond_5

    aget-byte p0, p2, v3

    if-nez p0, :cond_5

    aget-byte p0, p2, v2

    if-ne p0, p1, :cond_5

    const/4 p0, 0x3

    aget-byte p0, p2, p0

    if-ne p0, v1, :cond_5

    const-string p0, "UCS-4"

    goto :goto_0

    :cond_5
    invoke-static {}, Lcpdetector/io/UnknownCharset;->getInstance()Ljava/nio/charset/Charset;

    move-result-object p0

    return-object p0
.end method

.method public detectCodepage(Ljava/net/URL;)Ljava/nio/charset/Charset;
    .locals 1

    new-instance v0, Ljava/io/BufferedInputStream;

    invoke-virtual {p1}, Ljava/net/URL;->openStream()Ljava/io/InputStream;

    move-result-object p1

    invoke-direct {v0, p1}, Ljava/io/BufferedInputStream;-><init>(Ljava/io/InputStream;)V

    const p1, 0x7fffffff

    invoke-virtual {p0, v0, p1}, Lcpdetector/io/UnicodeDetector;->detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;

    move-result-object p0

    invoke-virtual {v0}, Ljava/io/BufferedInputStream;->close()V

    return-object p0
.end method
