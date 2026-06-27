.class public abstract Lcpdetector/io/AbstractCodepageDetector;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcpdetector/io/ICodepageDetector;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public compareTo(Ljava/lang/Object;)I
    .locals 0

    invoke-virtual {p1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object p1

    invoke-virtual {p1}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object p0

    invoke-virtual {p0}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p0, p1}, Ljava/lang/String;->compareTo(Ljava/lang/String;)I

    move-result p0

    return p0
.end method

.method public detectCodepage(Ljava/net/URL;)Ljava/nio/charset/Charset;
    .locals 1

    new-instance v0, Ljava/io/BufferedInputStream;

    invoke-virtual {p1}, Ljava/net/URL;->openStream()Ljava/io/InputStream;

    move-result-object p1

    invoke-direct {v0, p1}, Ljava/io/BufferedInputStream;-><init>(Ljava/io/InputStream;)V

    const p1, 0x7fffffff

    invoke-interface {p0, v0, p1}, Lcpdetector/io/ICodepageDetector;->detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;

    move-result-object p0

    invoke-virtual {v0}, Ljava/io/BufferedInputStream;->close()V

    return-object p0
.end method

.method public final open(Ljava/net/URL;)Ljava/io/Reader;
    .locals 2

    invoke-virtual {p0, p1}, Lcpdetector/io/AbstractCodepageDetector;->detectCodepage(Ljava/net/URL;)Ljava/nio/charset/Charset;

    move-result-object p0

    if-eqz p0, :cond_0

    new-instance v0, Ljava/io/InputStreamReader;

    new-instance v1, Ljava/io/BufferedInputStream;

    invoke-virtual {p1}, Ljava/net/URL;->openStream()Ljava/io/InputStream;

    move-result-object p1

    invoke-direct {v1, p1}, Ljava/io/BufferedInputStream;-><init>(Ljava/io/InputStream;)V

    invoke-direct {v0, v1, p0}, Ljava/io/InputStreamReader;-><init>(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return-object v0
.end method
