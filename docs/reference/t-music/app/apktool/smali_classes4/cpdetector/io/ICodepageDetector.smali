.class public interface abstract Lcpdetector/io/ICodepageDetector;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/io/Serializable;
.implements Ljava/lang/Comparable;


# virtual methods
.method public abstract detectCodepage(Ljava/io/InputStream;I)Ljava/nio/charset/Charset;
.end method

.method public abstract detectCodepage(Ljava/net/URL;)Ljava/nio/charset/Charset;
.end method

.method public abstract open(Ljava/net/URL;)Ljava/io/Reader;
.end method
