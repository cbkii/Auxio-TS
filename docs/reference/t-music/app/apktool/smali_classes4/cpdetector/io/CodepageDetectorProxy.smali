.class public final Lcpdetector/io/CodepageDetectorProxy;
.super Lcpdetector/io/AbstractCodepageDetector;
.source ""


# static fields
.field public static instance:Lcpdetector/io/CodepageDetectorProxy; = null

.field public static final serialVersionUID:J = -0x668c828956a6827dL


# instance fields
.field public detectors:Ljava/util/Set;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/Set<",
            "Lcpdetector/io/ICodepageDetector;",
            ">;"
        }
    .end annotation
.end field


# direct methods
.method public static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lcpdetector/io/AbstractCodepageDetector;-><init>()V

    new-instance v0, Ljava/util/LinkedHashSet;

    invoke-direct {v0}, Ljava/util/LinkedHashSet;-><init>()V

    iput-object v0, p0, Lcpdetector/io/CodepageDetectorProxy;->detectors:Ljava/util/Set;

    return-void
.end method

.method public static getInstance()Lcpdetector/io/CodepageDetectorProxy;
    .locals 1

    sget-object v0, Lcpdetector/io/CodepageDetectorProxy;->instance:Lcpdetector/io/CodepageDetectorProxy;

    if-nez v0, :cond_0

    new-instance v0, Lcpdetector/io/CodepageDetectorProxy;

    invoke-direct {v0}, Lcpdetector/io/CodepageDetectorProxy;-><init>()V

    sput-object v0, Lcpdetector/io/CodepageDetectorProxy;->instance:Lcpdetector/io/CodepageDetectorProxy;

    :cond_0
    sget-object v0, Lcpdetector/io/CodepageDetectorProxy;->instance:Lcpdetector/io/CodepageDetectorProxy;

    return-object v0
.end method


# virtual methods
.method public add(Lcpdetector/io/ICodepageDetector;)Z
    .locals 0

    iget-object p0, p0, Lcpdetector/io/CodepageDetectorProxy;->detectors:Ljava/util/Set;

    invoke-interface {p0, p1}, Ljava/util/Set;->add(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public clearDetectors()V
    .locals 0

    iget-object p0, p0, Lcpdetector/io/CodepageDetectorProxy;->detectors:Ljava/util/Set;

    invoke-interface {p0}, Ljava/util/Set;->clear()V

    return-void
.end method

.method public detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;
    .locals 2

    invoke-virtual {p1}, Ljava/io/InputStream;->markSupported()Z

    move-result v0

    if-eqz v0, :cond_2

    const/4 v0, 0x0

    iget-object p0, p0, Lcpdetector/io/CodepageDetectorProxy;->detectors:Ljava/util/Set;

    invoke-interface {p0}, Ljava/util/Set;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :cond_0
    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-virtual {p1, p2}, Ljava/io/InputStream;->mark(I)V

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcpdetector/io/ICodepageDetector;

    invoke-interface {v0, p1, p2}, Lcpdetector/io/ICodepageDetector;->detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;

    move-result-object v0

    :try_start_0
    invoke-virtual {p1}, Ljava/io/InputStream;->reset()V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    if-eqz v0, :cond_0

    invoke-static {}, Lcpdetector/io/UnknownCharset;->getInstance()Ljava/nio/charset/Charset;

    move-result-object v1

    if-eq v0, v1, :cond_0

    instance-of v1, v0, Lcpdetector/io/UnsupportedCharset;

    if-eqz v1, :cond_1

    goto :goto_0

    :catch_0
    move-exception p0

    new-instance p1, Ljava/lang/IllegalStateException;

    const-string p2, "More than the given length had to be read and the given stream could not be reset. Undetermined state for this detection."

    invoke-direct {p1, p2}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    invoke-virtual {p1, p0}, Ljava/lang/IllegalStateException;->initCause(Ljava/lang/Throwable;)Ljava/lang/Throwable;

    throw p1

    :cond_1
    return-object v0

    :cond_2
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p2, "The given input stream ("

    invoke-static {p2}, La/a/a/a/a;->a(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    invoke-virtual {p1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object p1

    invoke-virtual {p1}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ") has to support for marking."

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public detectCodepage(Ljava/net/URL;)Ljava/nio/charset/Charset;
    .locals 2

    iget-object p0, p0, Lcpdetector/io/CodepageDetectorProxy;->detectors:Ljava/util/Set;

    invoke-interface {p0}, Ljava/util/Set;->iterator()Ljava/util/Iterator;

    move-result-object p0

    const/4 v0, 0x0

    :cond_0
    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcpdetector/io/ICodepageDetector;

    invoke-interface {v0, p1}, Lcpdetector/io/ICodepageDetector;->detectCodepage(Ljava/net/URL;)Ljava/nio/charset/Charset;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-static {}, Lcpdetector/io/UnknownCharset;->getInstance()Ljava/nio/charset/Charset;

    move-result-object v1

    if-eq v0, v1, :cond_0

    instance-of v1, v0, Lcpdetector/io/UnsupportedCharset;

    if-eqz v1, :cond_1

    goto :goto_0

    :cond_1
    return-object v0
.end method

.method public toString()Ljava/lang/String;
    .locals 4

    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    iget-object p0, p0, Lcpdetector/io/CodepageDetectorProxy;->detectors:Ljava/util/Set;

    invoke-interface {p0}, Ljava/util/Set;->iterator()Ljava/util/Iterator;

    move-result-object p0

    const/4 v1, 0x1

    move v2, v1

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-virtual {v0, v2}, Ljava/lang/StringBuffer;->append(I)Ljava/lang/StringBuffer;

    const-string v3, ") "

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lcpdetector/io/ICodepageDetector;

    invoke-virtual {v3}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const-string v3, "\n"

    invoke-virtual {v0, v3}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    add-int/2addr v2, v1

    goto :goto_0

    :cond_0
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
