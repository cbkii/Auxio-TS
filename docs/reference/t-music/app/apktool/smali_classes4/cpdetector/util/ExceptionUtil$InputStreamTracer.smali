.class public Lcpdetector/util/ExceptionUtil$InputStreamTracer;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/lang/Runnable;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcpdetector/util/ExceptionUtil;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x9
    name = "InputStreamTracer"
.end annotation


# instance fields
.field public m_charset:Ljava/nio/charset/Charset;

.field public m_match:Ljava/lang/String;

.field public m_matched:Z

.field public m_streamToTrace:Ljava/io/InputStream;


# direct methods
.method public constructor <init>(Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/charset/Charset;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcpdetector/util/ExceptionUtil$InputStreamTracer;->m_streamToTrace:Ljava/io/InputStream;

    iput-object p2, p0, Lcpdetector/util/ExceptionUtil$InputStreamTracer;->m_match:Ljava/lang/String;

    iput-object p3, p0, Lcpdetector/util/ExceptionUtil$InputStreamTracer;->m_charset:Ljava/nio/charset/Charset;

    return-void
.end method


# virtual methods
.method public isMatched()Z
    .locals 0

    iget-boolean p0, p0, Lcpdetector/util/ExceptionUtil$InputStreamTracer;->m_matched:Z

    return p0
.end method

.method public run()V
    .locals 4

    new-instance v0, Ljava/io/BufferedReader;

    new-instance v1, Ljava/io/InputStreamReader;

    iget-object v2, p0, Lcpdetector/util/ExceptionUtil$InputStreamTracer;->m_streamToTrace:Ljava/io/InputStream;

    iget-object v3, p0, Lcpdetector/util/ExceptionUtil$InputStreamTracer;->m_charset:Ljava/nio/charset/Charset;

    invoke-direct {v1, v2, v3}, Ljava/io/InputStreamReader;-><init>(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V

    invoke-direct {v0, v1}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V

    :cond_0
    :try_start_0
    invoke-virtual {v0}, Ljava/io/BufferedReader;->readLine()Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_1

    iget-object v2, p0, Lcpdetector/util/ExceptionUtil$InputStreamTracer;->m_match:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v2

    if-eqz v2, :cond_1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcpdetector/util/ExceptionUtil$InputStreamTracer;->m_matched:Z
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :cond_1
    if-nez v1, :cond_0

    :goto_0
    return-void

    :catch_0
    move-exception p0

    new-instance v0, Ljava/lang/RuntimeException;

    invoke-direct {v0, p0}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/Throwable;)V

    throw v0
.end method
