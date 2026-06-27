.class public final Lcpdetector/util/ExceptionUtil;
.super Ljava/lang/Object;
.source ""


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcpdetector/util/ExceptionUtil$InputStreamTracer;
    }
.end annotation


# static fields
.field public static instance:Lcpdetector/util/ExceptionUtil;


# direct methods
.method public static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static captureSystemErrForDebuggingPurposesOnly(Z)Ljava/io/InputStream;
    .locals 3

    new-instance v0, Ljava/io/PipedOutputStream;

    invoke-direct {v0}, Ljava/io/PipedOutputStream;-><init>()V

    new-instance v1, Ljava/io/PipedInputStream;

    invoke-direct {v1, v0}, Ljava/io/PipedInputStream;-><init>(Ljava/io/PipedOutputStream;)V

    if-eqz p0, :cond_0

    new-instance p0, Lcpdetector/io/MultiplexingOutputStream;

    sget-object v2, Ljava/lang/System;->err:Ljava/io/PrintStream;

    invoke-direct {p0, v2, v0}, Lcpdetector/io/MultiplexingOutputStream;-><init>(Ljava/io/OutputStream;Ljava/io/OutputStream;)V

    goto :goto_0

    :cond_0
    move-object p0, v0

    :goto_0
    new-instance v0, Ljava/io/PrintStream;

    invoke-direct {v0, p0}, Ljava/io/PrintStream;-><init>(Ljava/io/OutputStream;)V

    invoke-static {v0}, Ljava/lang/System;->setErr(Ljava/io/PrintStream;)V

    return-object v1
.end method

.method public static captureSystemOutForDebuggingPurposesOnly(Z)Ljava/io/InputStream;
    .locals 3

    new-instance v0, Ljava/io/PipedOutputStream;

    invoke-direct {v0}, Ljava/io/PipedOutputStream;-><init>()V

    new-instance v1, Ljava/io/PipedInputStream;

    invoke-direct {v1, v0}, Ljava/io/PipedInputStream;-><init>(Ljava/io/PipedOutputStream;)V

    if-eqz p0, :cond_0

    new-instance p0, Lcpdetector/io/MultiplexingOutputStream;

    sget-object v2, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-direct {p0, v2, v0}, Lcpdetector/io/MultiplexingOutputStream;-><init>(Ljava/io/OutputStream;Ljava/io/OutputStream;)V

    goto :goto_0

    :cond_0
    move-object p0, v0

    :goto_0
    new-instance v0, Ljava/io/PrintStream;

    invoke-direct {v0, p0}, Ljava/io/PrintStream;-><init>(Ljava/io/OutputStream;)V

    invoke-static {v0}, Ljava/lang/System;->setOut(Ljava/io/PrintStream;)V

    return-object v1
.end method

.method public static dumpThreadStack(Ljava/io/PrintStream;)V
    .locals 2

    invoke-static {}, Ljava/lang/Thread;->currentThread()Ljava/lang/Thread;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Thread;->getStackTrace()[Ljava/lang/StackTraceElement;

    move-result-object v0

    const-string v1, "\n"

    invoke-static {v0, v1}, Lcpdetector/util/StringUtil;->arrayToString(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/io/PrintStream;->println(Ljava/lang/String;)V

    return-void
.end method

.method public static findMatchInSystemErr(Ljava/lang/String;)Lcpdetector/util/ExceptionUtil$InputStreamTracer;
    .locals 4

    const/4 v0, 0x1

    invoke-static {v0}, Lcpdetector/util/ExceptionUtil;->captureSystemErrForDebuggingPurposesOnly(Z)Ljava/io/InputStream;

    move-result-object v1

    new-instance v2, Lcpdetector/util/ExceptionUtil$InputStreamTracer;

    invoke-static {}, Ljava/nio/charset/Charset;->defaultCharset()Ljava/nio/charset/Charset;

    move-result-object v3

    invoke-direct {v2, v1, p0, v3}, Lcpdetector/util/ExceptionUtil$InputStreamTracer;-><init>(Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/charset/Charset;)V

    new-instance p0, Ljava/lang/Thread;

    invoke-direct {p0, v2}, Ljava/lang/Thread;-><init>(Ljava/lang/Runnable;)V

    invoke-virtual {p0, v0}, Ljava/lang/Thread;->setDaemon(Z)V

    invoke-virtual {p0}, Ljava/lang/Thread;->start()V

    return-object v2
.end method

.method public static findMatchInSystemOut(Ljava/lang/String;)Lcpdetector/util/ExceptionUtil$InputStreamTracer;
    .locals 4

    const/4 v0, 0x1

    invoke-static {v0}, Lcpdetector/util/ExceptionUtil;->captureSystemOutForDebuggingPurposesOnly(Z)Ljava/io/InputStream;

    move-result-object v1

    new-instance v2, Lcpdetector/util/ExceptionUtil$InputStreamTracer;

    invoke-static {}, Ljava/nio/charset/Charset;->defaultCharset()Ljava/nio/charset/Charset;

    move-result-object v3

    invoke-direct {v2, v1, p0, v3}, Lcpdetector/util/ExceptionUtil$InputStreamTracer;-><init>(Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/charset/Charset;)V

    new-instance p0, Ljava/lang/Thread;

    invoke-direct {p0, v2}, Ljava/lang/Thread;-><init>(Ljava/lang/Runnable;)V

    invoke-virtual {p0, v0}, Ljava/lang/Thread;->setDaemon(Z)V

    invoke-virtual {p0}, Ljava/lang/Thread;->start()V

    return-object v2
.end method

.method public static instance()Lcpdetector/util/ExceptionUtil;
    .locals 1

    sget-object v0, Lcpdetector/util/ExceptionUtil;->instance:Lcpdetector/util/ExceptionUtil;

    if-nez v0, :cond_0

    new-instance v0, Lcpdetector/util/ExceptionUtil;

    invoke-direct {v0}, Lcpdetector/util/ExceptionUtil;-><init>()V

    sput-object v0, Lcpdetector/util/ExceptionUtil;->instance:Lcpdetector/util/ExceptionUtil;

    :cond_0
    sget-object v0, Lcpdetector/util/ExceptionUtil;->instance:Lcpdetector/util/ExceptionUtil;

    return-object v0
.end method
