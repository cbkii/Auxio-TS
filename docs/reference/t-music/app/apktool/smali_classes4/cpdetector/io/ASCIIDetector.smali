.class public final Lcpdetector/io/ASCIIDetector;
.super Lcpdetector/io/AbstractCodepageDetector;
.source ""


# static fields
.field public static instance:Lcpdetector/io/ICodepageDetector; = null

.field public static final serialVersionUID:J = 0x3431313339333135L


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lcpdetector/io/AbstractCodepageDetector;-><init>()V

    return-void
.end method

.method public static getInstance()Lcpdetector/io/ICodepageDetector;
    .locals 1

    sget-object v0, Lcpdetector/io/ASCIIDetector;->instance:Lcpdetector/io/ICodepageDetector;

    if-nez v0, :cond_0

    new-instance v0, Lcpdetector/io/ASCIIDetector;

    invoke-direct {v0}, Lcpdetector/io/ASCIIDetector;-><init>()V

    sput-object v0, Lcpdetector/io/ASCIIDetector;->instance:Lcpdetector/io/ICodepageDetector;

    :cond_0
    sget-object v0, Lcpdetector/io/ASCIIDetector;->instance:Lcpdetector/io/ICodepageDetector;

    return-object v0
.end method


# virtual methods
.method public detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;
    .locals 1

    invoke-static {}, Lcpdetector/io/UnknownCharset;->getInstance()Ljava/nio/charset/Charset;

    move-result-object p0

    instance-of p2, p1, Ljava/io/BufferedInputStream;

    if-nez p2, :cond_0

    new-instance p2, Ljava/io/BufferedInputStream;

    const/16 v0, 0x1000

    invoke-direct {p2, p1, v0}, Ljava/io/BufferedInputStream;-><init>(Ljava/io/InputStream;I)V

    move-object p1, p2

    :cond_0
    invoke-static {p1}, Lcpdetector/util/FileUtil;->isAllASCII(Ljava/io/InputStream;)Z

    move-result p1

    if-eqz p1, :cond_1

    const-string p0, "US-ASCII"

    invoke-static {p0}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object p0

    :cond_1
    return-object p0
.end method
