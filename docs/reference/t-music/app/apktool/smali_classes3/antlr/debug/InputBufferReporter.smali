.class public Lantlr/debug/InputBufferReporter;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lantlr/debug/InputBufferListener;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public doneParsing(Lantlr/debug/TraceEvent;)V
    .locals 0

    return-void
.end method

.method public inputBufferChanged(Lantlr/debug/InputBufferEvent;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/Object;)V

    return-void
.end method

.method public inputBufferConsume(Lantlr/debug/InputBufferEvent;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/Object;)V

    return-void
.end method

.method public inputBufferLA(Lantlr/debug/InputBufferEvent;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/Object;)V

    return-void
.end method

.method public inputBufferMark(Lantlr/debug/InputBufferEvent;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/Object;)V

    return-void
.end method

.method public inputBufferRewind(Lantlr/debug/InputBufferEvent;)V
    .locals 0

    sget-object p0, Ljava/lang/System;->out:Ljava/io/PrintStream;

    invoke-virtual {p0, p1}, Ljava/io/PrintStream;->println(Ljava/lang/Object;)V

    return-void
.end method

.method public refresh()V
    .locals 0

    return-void
.end method
