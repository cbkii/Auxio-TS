.class public Lcpdetector/io/HTMLCodepageDetector;
.super Lcpdetector/io/AbstractCodepageDetector;
.source ""


# static fields
.field public static final serialVersionUID:J = 0x2d37393536303733L


# instance fields
.field public delegate:Lcpdetector/io/ParsingDetector;


# direct methods
.method public constructor <init>()V
    .locals 1

    const/4 v0, 0x0

    invoke-direct {p0, v0}, Lcpdetector/io/HTMLCodepageDetector;-><init>(Z)V

    return-void
.end method

.method public constructor <init>(Z)V
    .locals 1

    invoke-direct {p0}, Lcpdetector/io/AbstractCodepageDetector;-><init>()V

    new-instance v0, Lcpdetector/io/ParsingDetector;

    invoke-direct {v0, p1}, Lcpdetector/io/ParsingDetector;-><init>(Z)V

    iput-object v0, p0, Lcpdetector/io/HTMLCodepageDetector;->delegate:Lcpdetector/io/ParsingDetector;

    return-void
.end method


# virtual methods
.method public compareTo(Ljava/lang/Object;)I
    .locals 0

    iget-object p0, p0, Lcpdetector/io/HTMLCodepageDetector;->delegate:Lcpdetector/io/ParsingDetector;

    invoke-virtual {p0, p1}, Lcpdetector/io/AbstractCodepageDetector;->compareTo(Ljava/lang/Object;)I

    move-result p0

    return p0
.end method

.method public detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;
    .locals 0

    iget-object p0, p0, Lcpdetector/io/HTMLCodepageDetector;->delegate:Lcpdetector/io/ParsingDetector;

    invoke-virtual {p0, p1, p2}, Lcpdetector/io/ParsingDetector;->detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;

    move-result-object p0

    return-object p0
.end method

.method public detectCodepage(Ljava/net/URL;)Ljava/nio/charset/Charset;
    .locals 0

    iget-object p0, p0, Lcpdetector/io/HTMLCodepageDetector;->delegate:Lcpdetector/io/ParsingDetector;

    invoke-virtual {p0, p1}, Lcpdetector/io/AbstractCodepageDetector;->detectCodepage(Ljava/net/URL;)Ljava/nio/charset/Charset;

    move-result-object p0

    return-object p0
.end method

.method public equals(Ljava/lang/Object;)Z
    .locals 0

    iget-object p0, p0, Lcpdetector/io/HTMLCodepageDetector;->delegate:Lcpdetector/io/ParsingDetector;

    invoke-virtual {p0, p1}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public hashCode()I
    .locals 0

    iget-object p0, p0, Lcpdetector/io/HTMLCodepageDetector;->delegate:Lcpdetector/io/ParsingDetector;

    invoke-virtual {p0}, Ljava/lang/Object;->hashCode()I

    move-result p0

    return p0
.end method

.method public toString()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcpdetector/io/HTMLCodepageDetector;->delegate:Lcpdetector/io/ParsingDetector;

    invoke-virtual {p0}, Ljava/lang/Object;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
