.class public Lcpdetector/io/UnknownCharset;
.super Ljava/nio/charset/Charset;
.source ""


# static fields
.field public static instance:Ljava/nio/charset/Charset;


# direct methods
.method public constructor <init>()V
    .locals 2

    const-string v0, "void"

    const/4 v1, 0x0

    invoke-direct {p0, v0, v1}, Ljava/nio/charset/Charset;-><init>(Ljava/lang/String;[Ljava/lang/String;)V

    return-void
.end method

.method public static getInstance()Ljava/nio/charset/Charset;
    .locals 1

    sget-object v0, Lcpdetector/io/UnknownCharset;->instance:Ljava/nio/charset/Charset;

    if-nez v0, :cond_0

    new-instance v0, Lcpdetector/io/UnknownCharset;

    invoke-direct {v0}, Lcpdetector/io/UnknownCharset;-><init>()V

    sput-object v0, Lcpdetector/io/UnknownCharset;->instance:Ljava/nio/charset/Charset;

    :cond_0
    sget-object v0, Lcpdetector/io/UnknownCharset;->instance:Ljava/nio/charset/Charset;

    return-object v0
.end method


# virtual methods
.method public contains(Ljava/nio/charset/Charset;)Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public newDecoder()Ljava/nio/charset/CharsetDecoder;
    .locals 1

    new-instance p0, Ljava/lang/UnsupportedOperationException;

    const-string v0, "This is no real Charset but a flag you should test for!"

    invoke-direct {p0, v0}, Ljava/lang/UnsupportedOperationException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public newEncoder()Ljava/nio/charset/CharsetEncoder;
    .locals 1

    new-instance p0, Ljava/lang/UnsupportedOperationException;

    const-string v0, "This is no real Charset but a flag you should test for!"

    invoke-direct {p0, v0}, Ljava/lang/UnsupportedOperationException;-><init>(Ljava/lang/String;)V

    throw p0
.end method
